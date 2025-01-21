package com.finapp.backend.unit.security;


import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.TokenException;
import com.finapp.backend.security.enums.Role;
import com.finapp.backend.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Spy
    @InjectMocks
    private JwtService jwtService;



    private UserDetails userDetails;
    //This is a test secret, has no security risk. Only for this test class.
    private static final String TEST_JWT_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String TEST_USERNAME = "user@finapp.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_ISSUER = "finapp-backend";
    private static final String TEST_AUDIENCE = "finapp-frontend";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> userAuthorities = Role.USER.getAuthorities();

        // Setup test user
        userDetails = new User(
                TEST_USERNAME,
                TEST_PASSWORD,
                userAuthorities
        );

        // Inject properties using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "JWT_SECRET", TEST_JWT_SECRET);
        ReflectionTestUtils.setField(jwtService, "JWT_ISSUER", TEST_ISSUER);
        ReflectionTestUtils.setField(jwtService, "JWT_AUDIENCE", TEST_AUDIENCE);
        ReflectionTestUtils.setField(jwtService, "ACCESS_TOKEN_EXPIRATION", ACCESS_TOKEN_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "REFRESH_TOKEN_EXPIRATION", REFRESH_TOKEN_EXPIRATION);


    }

    @Nested
    @DisplayName("Token Generation Tests")
    class TokenGenerationTests {

        @Test
        @DisplayName("Should generate valid access token with correct claims")
        void shouldGenerateValidAccessToken() {
            // Act
            String token = jwtService.generateAccessToken(userDetails);

            // Assert
            assertNotNull(token);
            assertEquals(TEST_USERNAME, jwtService.extractUsername(token));
            assertTrue(jwtService.isTokenValid(token, userDetails));

            // Verify claims
            Map<String, Object> claims = jwtService.createUserClaims(userDetails);
            assertThat(claims)
                    .containsKey("authorities")
                    .containsKey("username")
                    .hasSize(2);
        }

        @Test
        @DisplayName("Should generate valid refresh token")
        void shouldGenerateValidRefreshToken() {
            // Act
            String token = jwtService.generateRefreshToken(userDetails);

            // Assert
            assertNotNull(token);
            assertEquals(TEST_USERNAME, jwtService.extractUsername(token));
            assertTrue(jwtService.isTokenValid(token, userDetails));
        }

        @Test
        @DisplayName("Should generate different tokens for same user")
        void shouldGenerateDifferentTokensForSameUser() {
            // Act
            String token1 = jwtService.generateAccessToken(userDetails);
            String token2 = jwtService.generateRefreshToken(userDetails);

            // Assert
            assertNotEquals(token1, token2);
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should invalidate token with incorrect user details")
        void shouldInvalidateTokenWithIncorrectUserDetails() {

            String token = jwtService.generateAccessToken(userDetails);
            UserDetails wrongUser = new User(
                    "user@wrong.com",
                    "password",
                    Collections.emptyList()
            );

            assertFalse(jwtService.isTokenValid(token, wrongUser));
        }

    }

    @Nested
    @DisplayName("Claims Extraction Tests")
    class ClaimsExtractionTests {


        @Test
        @DisplayName("Should extract username from token")
        void shouldExtractUsernameFromToken() {
            // Arrange
            String token = jwtService.generateAccessToken(userDetails);

            // Act
            String username = jwtService.extractUsername(token);

            // Assert
            assertEquals(TEST_USERNAME, username);
        }

        @Test
        @DisplayName("Should extract authorities from token")
        void shouldExtractAuthoritiesFromToken() {
            // Arrange
            String token = jwtService.generateAccessToken(userDetails);

            // Act
            @SuppressWarnings("rawtypes")
            List authorities = jwtService.extractAnyClaim(
                    token,
                    claim -> claim.get("authorities", List.class)
            );

            // Assert
            assertNotNull(authorities);


            List<String> expectedAuthorities = Role.USER.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).sorted().collect(Collectors.toList());

            // Sort both lists to compare the values ignoring the order
            //noinspection unchecked
            Collections.sort(authorities);

            // Assert
            assertIterableEquals(expectedAuthorities, authorities);
        }

        @Test
        @DisplayName("Should throw exception for malformed token")
        void shouldThrowExceptionForMalformedToken() {
            assertThrows(CustomFinAppException.class, () ->
                    jwtService.extractUsername("a.malformed.token")
            );
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle invalid secret key")
        void shouldHandleInvalidSecretKey() {
            // Arrange
            ReflectionTestUtils.setField(jwtService, "JWT_SECRET", "invalid-secret");

            // Act & Assert
            assertThrows(CustomFinAppException.class, () ->
                    jwtService.generateAccessToken(userDetails)
            );
        }

        @Test
        @DisplayName("Should handle null user details")
        void shouldHandleNullUserDetails() {
            // Act & Assert
            assertThrows(TokenException.class, () ->
                    jwtService.generateAccessToken(null)
            );
        }

        @Test
        @DisplayName("Should handle empty claims")
        void shouldHandleEmptyClaims() {

            UserDetails userWithEmptyClaims = new User("user", "password", Collections.emptyList());

            // Act & Assert
            assertThrows(CustomFinAppException.class,  () ->
                    jwtService.generateAccessToken(userWithEmptyClaims)
            );
        }
    }

    @Test
    @DisplayName("Should create user claims with correct attributes")
    void shouldCreateUserClaimsWithCorrectAttributes() {
        // Act
        Map<String, Object> claims = jwtService.createUserClaims(userDetails);

        // Assert
        assertThat(claims)
                .containsEntry("username", TEST_USERNAME)
                .containsKey("authorities");

        Object authorities =  claims.get("authorities");

        // Sort both lists to compare the values ignoring the order
        List<String> expectedAuthorities = Role.USER.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        @SuppressWarnings("unchecked")
        List<String> actualAuthorities = (List<String>) authorities;

        Collections.sort(expectedAuthorities);
        Collections.sort(actualAuthorities);

        // Assert
        assertIterableEquals(expectedAuthorities, actualAuthorities);

    }
}