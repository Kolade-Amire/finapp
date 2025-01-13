package com.finapp.backend.interfaces.service;

import com.finapp.backend.domain.Token;

public interface TokenService {
    Token findByUserId(String userId);

    Token findByToken(String token);

    void saveToken(Token token);

    void deleteTokenByUserId(String userId);
}
