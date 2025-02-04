package com.finapp.backend.security.repository;

import com.finapp.backend.security.entity.Token;

public interface TokenService {
    Token findByUserId(String userId);

    Token findByToken(String token);

    void saveToken(Token token);

    void deleteTokenByUserId(String userId);
}
