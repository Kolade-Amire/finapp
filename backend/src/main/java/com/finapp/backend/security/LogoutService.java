package com.finapp.backend.security;

import com.finapp.backend.entity.Token;
import com.finapp.backend.exception.TokenException;
import com.finapp.backend.repository.RedisTokenRepository;
import com.finapp.backend.service.TokenService;
import com.finapp.backend.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutService.class);

    private final RedisTokenRepository tokenRepository;
    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        if(authHeader == null || !authHeader.startsWith(AppConstants.TOKEN_PREFIX))
            return;
        token = authHeader.substring(AppConstants.TOKEN_PREFIX.length());
        try {
            Token savedToken = tokenService.findByToken(token);
            savedToken.setExpired(true);
            savedToken.setRevoked(true);
            tokenService.saveToken(savedToken);
            SecurityContextHolder.clearContext();
        } catch (TokenException e) {
           LOGGER.error("Token not found");
        }

    }
}
