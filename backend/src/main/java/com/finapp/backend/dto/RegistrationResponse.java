package com.finapp.backend.dto;

import com.finapp.backend.util.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
public class RegistrationResponse {
    private HttpResponse httpResponse;
    private String accessToken;
    private UserAuthenticationDto user;
}
