package com.finapp.backend.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.security.entity.Token;
import com.finapp.backend.security.dto.AuthenticationRequest;
import com.finapp.backend.security.dto.AuthenticationResponse;
import com.finapp.backend.security.dto.RegistrationRequest;
import com.finapp.backend.security.dto.RegistrationResponse;
import com.finapp.backend.user.entity.User;
import com.finapp.backend.user.dto.UserDto;
import com.finapp.backend.user.entity.UserPrincipal;
import com.finapp.backend.utils.HttpResponse;
import com.finapp.backend.dto.auth.*;
import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.EntityAlreadyExistException;
import com.finapp.backend.exception.PasswordsDoNotMatchException;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.security.repository.AuthenticationService;
import com.finapp.backend.security.repository.TokenService;
import com.finapp.backend.user.interfaces.UserService;
import com.finapp.backend.security.enums.Role;
import com.finapp.backend.utils.AppConstants;
import com.finapp.backend.user.dto.UserDtoMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    private String doPasswordsMatch(String p1, String p2) {
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

        validateNewUser(request.getEmail());
        var password = doPasswordsMatch(request.getPassword(), request.getConfirmPassword());

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .phoneNumber(request.getPhoneNumber())
                .isAccountExpired(false)
                .isEmailVerified(true)
                .build();

        UserDto savedUser = userService.saveUser(user);

        var httpResponse = HttpResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .httpStatusCode(HttpStatus.CREATED.value())
                .reason(HttpStatus.CREATED.getReasonPhrase())
                .message(AppConstants.REGISTRATION_SUCCESS)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        return RegistrationResponse.builder()
                .httpResponse(httpResponse)
                .user(savedUser)
                .build();

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
        } catch (AuthenticationException e) {
            LOGGER.error("Error from authenticationManager.authenticate()", e);
            throw new CustomFinAppException("An error occurred while trying to authenticate user. Please try again later.");
        }

        var user = userService.findByEmail(request.getEmail());

        user.setLastLoginAt(LocalDateTime.now(ZoneOffset.UTC));


        try {
            tokenService.deleteTokenByUserId(user.getId().toString());
        } catch (Exception e) {
            LOGGER.info("Failed to delete user's existing refresh token. Token might have expired which means it's been removed. \n More specific cause:{}", e.getMessage());
        }


        var userPrincipal = new UserPrincipal(user);

        String accessToken;
        String refreshToken;

        try {
            accessToken = jwtService.generateAccessToken(userPrincipal);
            refreshToken = jwtService.generateRefreshToken(userPrincipal);
        } catch (Exception e) {
            LOGGER.error("Error occurred while trying to generate refresh/access token(s)", e);
            throw new CustomFinAppException("An error occurred while authenticating user.");
        }

        saveUserRefreshToken(user, refreshToken);

        var response = HttpResponse.builder()
                .httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .reason(HttpStatus.OK.getReasonPhrase())
                .message(AppConstants.AUTHENTICATION_SUCCESS)
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        UserDto userDto = UserDtoMapper.mapUserToDto(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .httpResponse(response)
                .user(userDto)
                .build();

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
            throw new SaveEntityException(String.format("Error occurred while saving user token. User Id: %s", user.getId().toString()), e);
        }
    }

    @Override
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

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
                            .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                            .message(AppConstants.REFRESH_SUCCESS)
                            .build();

                    var authResponse = AuthenticationResponse.builder()
                            .httpResponse(customHttpResponse)
                            .accessToken(newAccessToken)
                            .build();

                    OutputStream outputStream = response.getOutputStream();

                    objectMapper.writeValue(outputStream, authResponse);
                    outputStream.flush();
                }

            }
        } catch (JwtException e) {
            LOGGER.error("A Jwt Error occurred.", e);
            throw new CustomFinAppException("Error refreshing token.");
        }catch (Exception e) {
            LOGGER.error("An unexpected error occurred", e);
            throw new CustomFinAppException("Error refreshing token.");
        }

    }
}
