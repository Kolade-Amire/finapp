package com.finapp.backend.unit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.config.TestJacksonConfig;
import com.finapp.backend.domain.Token;
import com.finapp.backend.domain.User;
import com.finapp.backend.domain.UserPrincipal;
import com.finapp.backend.dto.auth.*;
import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.EntityAlreadyExistException;
import com.finapp.backend.exception.PasswordsDoNotMatchException;
import com.finapp.backend.exception.TokenException;
import com.finapp.backend.interfaces.service.TokenService;
import com.finapp.backend.interfaces.service.UserService;
import com.finapp.backend.security.enums.Role;
import com.finapp.backend.security.service.JwtService;
import com.finapp.backend.service.AuthenticationServiceImpl;
import com.finapp.backend.utils.AppConstants;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import(TestJacksonConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<Token> tokenCaptor;

    private static final String TEST_EMAIL = "testEmail@finapp.com";
    private static final String TEST_PASSWORD = "Password123!";
    private static final String TEST_FIRSTNAME = "Frank";
    private static final String TEST_LASTNAME = "Ocean";
    private static final String TEST_PHONE = "+1234567890";
    private static final String TEST_ACCESS_TOKEN = "test.access.token";
    private static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private static final String PROFILE_PIC_URL = "https://picsum.photos/200";

    private User testUser;
    private RegistrationRequest registrationRequest;
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {

        // Setup test user
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .firstname(TEST_FIRSTNAME)
                .lastname(TEST_LASTNAME)
                .phoneNumber(TEST_PHONE)
                .role(Role.USER)
                .isEmailVerified(true)
                .isAccountExpired(false)
                .profilePictureUrl(PROFILE_PIC_URL)
                .build();

        // Setup registration request
        registrationRequest = RegistrationRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .confirmPassword(TEST_PASSWORD)
                .firstname(TEST_FIRSTNAME)
                .lastname(TEST_LASTNAME)
                .phoneNumber(TEST_PHONE)
                .build();

        // Setup authentication request
        authenticationRequest = new AuthenticationRequest(TEST_EMAIL, TEST_PASSWORD);
    }

    @Nested
    @DisplayName("User Registration Tests")
    class RegistrationTests {

        @Test
        @DisplayName("Should successfully register new user")
        void shouldRegisterNewUser() {

            //Arrange
            when(userService.userExists(TEST_EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userService.saveUser(any(User.class))).thenReturn(new UserDto());

            // Act
            RegistrationResponse response = authenticationService.register(registrationRequest);

            // Assert
            assertNotNull(response);
            assertNotNull(response.getUser());
            assertEquals(HttpStatus.CREATED, response.getHttpResponse().getHttpStatus());
            assertEquals(AppConstants.REGISTRATION_SUCCESS, response.getHttpResponse().getMessage());

            verify(userService).saveUser(userCaptor.capture());
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser)
                    .hasFieldOrPropertyWithValue("email", TEST_EMAIL)
                    .hasFieldOrPropertyWithValue("firstname", TEST_FIRSTNAME)
                    .hasFieldOrPropertyWithValue("role", Role.USER);

        }


        @Test
        @DisplayName("Should throw exception when passwords don't match")
        void shouldThrowExceptionWhenPasswordsDontMatch() {
            // Arrange
            registrationRequest.setConfirmPassword("differentPassword");

            // Act & Assert
            assertThrows(PasswordsDoNotMatchException.class,
                    () -> authenticationService.register(registrationRequest));
            verify(userService, never()).saveUser(any());
        }

        @Test
        @DisplayName("Should throw exception when user already exists")
        void shouldThrowExceptionWhenUserExists() {
            // Arrange
            when(userService.userExists(TEST_EMAIL)).thenReturn(true);

            // Act & Assert
            assertThrows(EntityAlreadyExistException.class,
                    () -> authenticationService.register(registrationRequest));
            verify(userService, never()).saveUser(any());
        }
    }


    @Nested
    @DisplayName("Authentication Tests")
    class AuthenticationTests {

        @Test
        @DisplayName("Should successfully authenticate user")
        void shouldAuthenticateUser() {
            // Arrange
            when(userService.findByEmail(TEST_EMAIL)).thenReturn(testUser);
            when(jwtService.generateAccessToken(any(UserPrincipal.class))).thenReturn(TEST_ACCESS_TOKEN);
            when(jwtService.generateRefreshToken(any(UserPrincipal.class))).thenReturn(TEST_REFRESH_TOKEN);

            // Act
            AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

            // Assert
            assertNotNull(response);
            assertEquals(TEST_ACCESS_TOKEN, response.getAccessToken());
            assertEquals(HttpStatus.OK, response.getHttpResponse().getHttpStatus());


            verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(
                    TEST_EMAIL,
                    TEST_PASSWORD)
            );
            verify(tokenService, times(1)).saveToken(tokenCaptor.capture());


            Token capturedToken = tokenCaptor.getValue();
            assertThat(capturedToken)
                    .hasFieldOrPropertyWithValue("token", TEST_REFRESH_TOKEN)
                    .hasFieldOrPropertyWithValue("isExpired", false)
                    .hasFieldOrPropertyWithValue("isRevoked", false);
        }

        @Test
        @DisplayName("Should throw exception on authentication failure")
        void shouldThrowExceptionOnAuthenticationFailure() {
            // Arrange
            doThrow(new BadCredentialsException("Bad credentials"))
                    .when(authenticationManager)
                    .authenticate(any(UsernamePasswordAuthenticationToken.class));

            // Act & Assert
            assertThrows(CustomFinAppException.class,
                    () -> authenticationService.authenticate(authenticationRequest));
            verify(tokenService, never()).saveToken(any());
        }

        @Test
        @DisplayName("Should handle token generation failure")
        void shouldHandleTokenGenerationFailure() {
            // Arrange
            when(userService.findByEmail(TEST_EMAIL)).thenReturn(testUser);
            when(jwtService.generateAccessToken(any(UserPrincipal.class)))
                    .thenThrow(new TokenException("Token generation failed"));

            // Act & Assert
            assertThrows(CustomFinAppException.class,
                    () -> authenticationService.authenticate(authenticationRequest));
            verify(tokenService, never()).saveToken(any());
        }
    }


    @Nested
    @DisplayName("Token Refresh Tests")
    class TokenRefreshTests {

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private ServletOutputStream outputStream;


        @Test
        @DisplayName("Should successfully refresh access token")
        void shouldRefreshAccessToken() throws IOException {

            // Arrange
            Token testTokenEntity = Token.builder()
                    .id(1L)
                    .token(TEST_REFRESH_TOKEN)
                    .isExpired(false)
                    .isRevoked(false)
                    .userId(testUser.getId().toString())
                    .build();


            String authHeader = "Bearer " + TEST_ACCESS_TOKEN;
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);
            when(jwtService.extractUsername(TEST_ACCESS_TOKEN)).thenReturn(TEST_EMAIL);
            when(userService.findByEmail(TEST_EMAIL)).thenReturn(testUser);
            when(tokenService.findByUserId(testUser.getId().toString()))
                    .thenReturn(testTokenEntity);
            when(jwtService.isTokenValid(anyString(), any(UserPrincipal.class))).thenReturn(true);
            when(jwtService.generateAccessToken(any(UserPrincipal.class))).thenReturn(TEST_ACCESS_TOKEN);
            when(response.getOutputStream()).thenReturn(outputStream);


            // Act & Assert
            assertDoesNotThrow(() -> authenticationService.refreshAccessToken(request, response));

            verify(jwtService, times(1)).generateAccessToken(any(UserPrincipal.class));
            verify(response, times(1)).getOutputStream();
            verify(outputStream, times(1)).flush();
        }

        @Test
        @DisplayName("Should handle missing authorization header")
        void shouldHandleMissingAuthHeader() {
            // Arrange
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

            // Act
            authenticationService.refreshAccessToken(request, response);

            // Assert
            verify(jwtService, never()).extractUsername(anyString());
            verify(jwtService, never()).generateAccessToken(any(UserPrincipal.class));
        }

        @Test
        @DisplayName("Should handle invalid token format")
        void shouldHandleInvalidTokenFormat() {
            // Arrange
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidFormat");

            // Act
            authenticationService.refreshAccessToken(request, response);

            // Assert
            verify(jwtService, never()).extractUsername(anyString());
            verify(jwtService, never()).generateAccessToken(any(UserPrincipal.class));
        }
    }

}
