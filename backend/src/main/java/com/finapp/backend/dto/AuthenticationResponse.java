package com.finapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private HttpResponse httpResponse;
    @JsonProperty("access_token")
    private String accessToken;
    private UserAuthenticationDto user;
}
