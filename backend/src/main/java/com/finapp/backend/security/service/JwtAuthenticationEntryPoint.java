package com.finapp.backend.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.utils.AppConstants;
import com.finapp.backend.utils.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        if (!response.isCommitted()){

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());

            var httpResponse = HttpResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .httpStatusCode(HttpStatus.FORBIDDEN.value())
                    .message(AppConstants.FORBIDDEN_MESSAGE)
                    .reason(HttpStatus.FORBIDDEN.getReasonPhrase())
                    .timestamp(LocalDateTime.now())
                    .build();


            objectMapper.writeValue(response.getOutputStream(), httpResponse);
        }

    }
}
