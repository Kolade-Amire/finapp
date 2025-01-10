package com.finapp.backend.service;

import com.finapp.backend.entity.Token;

public interface TokenService {
    Token findByUserId(String userId);

    Token findByToken(String token);

    void saveToken(Token token);

    void deleteTokenByUserId(String userId);
}
