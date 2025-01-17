package com.finapp.backend.dto.auth;

import com.finapp.backend.dto.HttpResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString(exclude = "accessToken")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationResponse {
    private HttpResponse httpResponse;
    private String accessToken;
    private UserDto user;
}
