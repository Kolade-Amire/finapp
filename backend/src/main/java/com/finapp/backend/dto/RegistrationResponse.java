package com.finapp.backend.dto;

import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationResponse {
    private HttpResponse httpResponse;
    private String accessToken;
    private UserAuthenticationDto user;
}
