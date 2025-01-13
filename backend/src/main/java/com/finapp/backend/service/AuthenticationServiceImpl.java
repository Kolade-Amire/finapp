package com.finapp.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.dto.AuthenticationRequest;
import com.finapp.backend.dto.AuthenticationResponse;
import com.finapp.backend.dto.RegistrationRequest;
import com.finapp.backend.dto.RegistrationResponse;
import com.finapp.backend.domain.Role;
import com.finapp.backend.domain.Token;
import com.finapp.backend.domain.User;
import com.finapp.backend.domain.UserPrincipal;
import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.EntityAlreadyExistException;
import com.finapp.backend.exception.PasswordsDoNotMatchException;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.interfaces.service.AuthenticationService;
import com.finapp.backend.interfaces.service.TokenService;
import com.finapp.backend.interfaces.service.UserService;
import com.finapp.backend.utils.mapper.UserAuthenticationDtoMapper;
import com.finapp.backend.security.service.JwtService;
import com.finapp.backend.utils.AppConstants;
import com.finapp.backend.dto.HttpResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    private String doPasswordsMatch (String p1, String p2){
        if (!p1.equals(p2)) {
            throw new PasswordsDoNotMatchException(AppConstants.PASSWORDS_MISMATCH);
        } else return p2;
    }

    private void validateNewUser(String email) {
        if (userService.userExists(email)) {
            throw new EntityAlreadyExistException(
                    String.format("User with email %s already exists", email)
            );
        }
    }

    @Override
    public RegistrationResponse register(RegistrationRequest request) {
        try {
            validateNewUser(request.getEmail());
            var password = doPasswordsMatch(request.getPassword(), request.getConfirmPassword());

            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(password))
                    .role(Role.USER)
                    .phoneNumber(request.getPhoneNumber())
                    .createdAt(LocalDateTime.now())
                    .isAccountExpired(false)
                    .isEmailVerified(true)
                    .passwordLastChangedDate(LocalDateTime.now())
                    .build();

            var savedUser = userService.saveUser(user);
            var userDto = UserAuthenticationDtoMapper.mapUserToUserAuthDto(savedUser);


            var httpResponse = HttpResponse.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .httpStatusCode(HttpStatus.CREATED.value())
                    .reason(HttpStatus.CREATED.getReasonPhrase())
                    .message(AppConstants.REGISTRATION_SUCCESS)
                    .timestamp(LocalDateTime.now())
                    .build();

            return RegistrationResponse.builder()
                    .httpResponse(httpResponse)
                    .user(userDto)
                    .build();

        } catch (EntityAlreadyExistException e) {
            throw new EntityAlreadyExistException(e.getMessage());
        } catch (PasswordsDoNotMatchException e){
            throw new PasswordsDoNotMatchException(e.getMessage());
        } catch(Exception e){
            throw new SaveEntityException("Error registering new user.", e);
        }

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userService.findByEmail(request.getEmail());

            user.setLastLoginAt(LocalDateTime.now());


            try {
                tokenService.deleteTokenByUserId(user.getId().toString());
            } catch (Exception e) {
                LOGGER.info("Token expired, so it's been removed. Failed to delete user's existing token.");
            }


            var userPrincipal = new UserPrincipal(user);

            var accessToken = jwtService.generateAccessToken(userPrincipal);
            var refreshToken = jwtService.generateRefreshToken(userPrincipal);

            saveUserRefreshToken(user, refreshToken);

            var response = HttpResponse.builder()
                    .httpStatusCode(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .reason(HttpStatus.OK.getReasonPhrase())
                    .message(AppConstants.AUTHENTICATION_SUCCESS)
                    .timestamp(LocalDateTime.now())
                    .build();

            var userDto = UserAuthenticationDtoMapper.mapUserToUserAuthDto(user);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .httpResponse(response)
                    .user(userDto)
                    .build();

        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid Email or Password.");
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Error Authenticating user.", e);
        } catch (Exception e){
            throw new AuthenticationServiceException("User Authentication failed.", e);
        }

    }

    private void saveUserRefreshToken(User user, String token) {

        var newTokenEntity = Token.builder()
                .userId(user.getId().toString())
                .token(token)
                .isExpired(false)
                .isRevoked(false)
                .build();
        try {
            tokenService.saveToken(newTokenEntity);
        } catch (Exception e) {
            throw new SaveEntityException(String.format("Error occurred while saving user token to redis. User Id: %s", user.getId().toString()), e);
        }
    }

    @Override
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(AppConstants.TOKEN_PREFIX)) {
            return;
        }

        try {
            accessToken = authHeader.substring(AppConstants.TOKEN_PREFIX.length());
            userEmail = jwtService.extractUsername(accessToken);

            if (userEmail != null) {
                var user = this.userService.findByEmail(userEmail);
                var userPrincipal = new UserPrincipal(user);
                var refreshToken = tokenService.findByUserId(user.getId().toString());

                if (jwtService.isTokenValid(refreshToken.getToken(), userPrincipal)) {
                    var newAccessToken = jwtService.generateAccessToken(userPrincipal);


                    var customHttpResponse = HttpResponse.builder()
                            .httpStatusCode(HttpStatus.OK.value())
                            .httpStatus(HttpStatus.OK)
                            .reason(HttpStatus.OK.getReasonPhrase())
                            .message(AppConstants.REFRESH_SUCCESS)
                            .build();

                    var authResponse = AuthenticationResponse.builder()
                            .httpResponse(customHttpResponse)
                            .accessToken(newAccessToken)
                            .build();

                    OutputStream outputStream = response.getOutputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(outputStream, authResponse);
                    outputStream.flush();
                }


            }
        } catch (JwtException e) {
            throw new CustomFinAppException("A Jwt Error occurred.", e);
        } catch (UsernameNotFoundException e) {
            throw new CustomFinAppException("User not found.", e);
        } catch (Exception e) {
            throw new CustomFinAppException("Error refreshing access token.", e);
        }

    }
}
