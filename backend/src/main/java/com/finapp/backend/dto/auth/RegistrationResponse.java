package com.finapp.backend.dto.auth;

import com.finapp.backend.dto.HttpResponse;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegistrationResponse {
    private HttpResponse httpResponse;
    private UserDto user;
}
