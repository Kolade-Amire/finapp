package com.finapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finapp.backend.util.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@ToString
@AllArgsConstructor
public class AuthenticationResponse {

    private HttpResponse httpResponse;
    @JsonProperty("access_token")
    private String accessToken;
    private UserAuthenticationDto user;
}
