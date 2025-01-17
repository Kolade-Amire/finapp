package com.finapp.backend.service;

import com.finapp.backend.domain.Token;
import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.TokenException;
import com.finapp.backend.interfaces.repository.RedisTokenRepository;
import com.finapp.backend.interfaces.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RedisTokenRepository tokenRepository;

    @Override
    public Token findByUserId(String userId) {
        return tokenRepository.findByUserId(userId).orElseThrow(
                () -> new TokenException(String.format("Token for user with ID %s does not exist", userId)
                )
        );
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(
                () -> new TokenException(String.format("Could not retrieve token: %s ", token)
                )
        );
    }

    @Override
    public void saveToken(Token token) {
        try {
            tokenRepository.save(token);
        } catch (Exception e) {
            throw new TokenException("Failed to save token.", e);
        }
    }

    @Override
    public void deleteTokenByUserId(String userId) {
        var token = findByUserId(userId);
        try {
            tokenRepository.delete(token);
        } catch (Exception e) {
            throw new CustomFinAppException("An unexpected error (from redis data) occurred while trying to delete user's refresh token");
        }
    }

}
