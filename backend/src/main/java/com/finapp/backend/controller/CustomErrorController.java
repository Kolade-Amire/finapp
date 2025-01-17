package com.finapp.backend.controller;

import com.finapp.backend.dto.HttpResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatusCode(status.value())
                .httpStatus(status)
                .reason(status.getReasonPhrase())
                .message("An error occurred.")
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<>(httpResponse, status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            return HttpStatus.valueOf(statusCode);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
