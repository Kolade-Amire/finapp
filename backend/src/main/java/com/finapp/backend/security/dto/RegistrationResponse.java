package com.finapp.backend.security.dto;

import com.finapp.backend.utils.HttpResponse;
import com.finapp.backend.user.dto.UserDto;
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
