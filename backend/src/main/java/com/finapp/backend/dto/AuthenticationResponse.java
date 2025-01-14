package com.finapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Setter
@Getter
@ToString(exclude = "accessToken")
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private HttpResponse httpResponse;
    private String accessToken;
    private UserAuthenticationDto user;
}
