package com.finapp.backend.dto.auth;

import com.finapp.backend.dto.HttpResponse;
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
    private UserDto user;
}
