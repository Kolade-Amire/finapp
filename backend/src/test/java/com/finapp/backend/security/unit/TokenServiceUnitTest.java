package com.finapp.backend.security.unit;

import com.finapp.backend.security.entity.Token;
import com.finapp.backend.exception.TokenException;
import com.finapp.backend.security.repository.RedisTokenRepository;
import com.finapp.backend.security.service.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TokenServiceUnitTest {

    @Mock
    private RedisTokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private static final String TEST_TOKEN_STRING = "Qheysa.WTywuiwuiwqdakd.f2167fdkfd";

    @Captor
    private ArgumentCaptor<Token> tokenArgumentCaptor;

    private Token testToken;

    @BeforeEach
    void setUp(){
        testToken = Token.builder()
                .id(4422L)
                .userId(UUID.randomUUID().toString())
                .token(TEST_TOKEN_STRING)
                .isRevoked(false)
                .isExpired(false)
                .build();
    }

    @Test
    @DisplayName("Should return token entity by user ID")
    void shouldReturnTokenEntityByUserId(){
        //Arrange
        when(tokenRepository.findByUserId(testToken.getUserId())).thenReturn(Optional.of(testToken));
        //Act
        Token token = tokenService.findByUserId(testToken.getUserId());
        //Assert
        assertNotNull(token);
        verify(tokenRepository, times(1)).findByUserId(testToken.getUserId());
        assertThat(token)
                .hasFieldOrPropertyWithValue("id", testToken.getId())
                .hasFieldOrPropertyWithValue("token", testToken.getToken())
                .hasFieldOrPropertyWithValue("userId", testToken.getUserId())
                .hasFieldOrPropertyWithValue("isExpired", testToken.isExpired())
        ;
    }


    @Test
    @DisplayName("Should return token entity by token value")
    void shouldReturnTokenEntityByTokenValue(){
        //Arrange
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.of(testToken));
        //Act
        Token token = tokenService.findByToken(testToken.getToken());
        //Assert
        assertNotNull(token);
        verify(tokenRepository, times(1)).findByToken(testToken.getToken());
        assertThat(token)
                .hasFieldOrPropertyWithValue("id", testToken.getId())
                .hasFieldOrPropertyWithValue("token", testToken.getToken())
                .hasFieldOrPropertyWithValue("userId", testToken.getUserId())
                .hasFieldOrPropertyWithValue("isExpired", testToken.isExpired())
        ;
    }

    @Test
    @DisplayName("Should throw exception when token doesn't exist and findByUserId is called")
    void shouldThrowExceptionWhenFindByUserIdAndTokenIsNotFound(){
        //Arrange
        String nonExistingUserId = UUID.randomUUID().toString();
        when(tokenRepository.findByUserId(nonExistingUserId)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(TokenException.class, () -> tokenService.findByUserId(nonExistingUserId));
        verify(tokenRepository, times(1)).findByUserId(nonExistingUserId);

    }

    @Test
    @DisplayName("Should throw exception when token doesn't exist and findByToken is called")
    void shouldThrowExceptionWhenFindByTokenAndTokenIsNotFound(){
        //Arrange
        String nonExistingTokenString = "invalid-token";
        when(tokenRepository.findByToken(nonExistingTokenString)).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(TokenException.class, () -> tokenService.findByToken(nonExistingTokenString));
        verify(tokenRepository, times(1)).findByToken(nonExistingTokenString);


    }

    @Test
    @DisplayName("Should save token and return nothing")
    void shouldSaveTokenAndReturnVoid(){
        //Arrange
        when(tokenRepository.save(testToken)).thenReturn(testToken);
        //Act
        tokenService.saveToken(testToken);

        //Assert
        verify(tokenRepository, times(1)).save(tokenArgumentCaptor.capture());
        Token capturedToken = tokenArgumentCaptor.getValue();

        assertNotNull(capturedToken);
        assertThat(capturedToken)
                .hasFieldOrPropertyWithValue("id", testToken.getId())
                .hasFieldOrPropertyWithValue("token", testToken.getToken())
                .hasFieldOrPropertyWithValue("userId", testToken.getUserId())
        ;

    }

    @Test
    @DisplayName("Should delete token")
    void shouldDeleteToken(){
        //Arrange
        when(tokenRepository.findByUserId(testToken.getUserId())).thenReturn(Optional.of(testToken));
        doNothing().when(tokenRepository).delete(any(Token.class));

        //Act
        tokenService.deleteTokenByUserId(testToken.getUserId());

        //Assert
        verify(tokenRepository, times(1)).findByUserId(testToken.getUserId());
        verify(tokenRepository, times(1)).delete(testToken);

    }
}
