package com.finapp.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.util.AppConstants;
import com.finapp.backend.util.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {
    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        var httpResponse = HttpResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .httpStatusCode(HttpStatus.FORBIDDEN.value())
                .message(AppConstants.FORBIDDEN_MESSAGE)
                .reason(HttpStatus.FORBIDDEN.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
