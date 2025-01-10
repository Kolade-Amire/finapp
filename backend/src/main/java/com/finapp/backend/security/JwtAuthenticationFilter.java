package com.finapp.backend.security;

import com.finapp.backend.entity.Token;
import com.finapp.backend.entity.UserPrincipal;
import com.finapp.backend.service.TokenService;
import com.finapp.backend.util.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().contains(AppConstants.API_VERSION)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith(AppConstants.TOKEN_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(AppConstants.TOKEN_PREFIX.length());
        userEmail = jwtService.extractUsername(token);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserPrincipal userPrincipal = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(token, userPrincipal) && !isTokenExpired(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else
                SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

    }

    protected boolean isTokenExpired(String token) {
        try {
            Token retrievedToken = tokenService.findByToken(token);
            return retrievedToken.isExpired() && retrievedToken.isRevoked();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return true;
        }

    }
}
