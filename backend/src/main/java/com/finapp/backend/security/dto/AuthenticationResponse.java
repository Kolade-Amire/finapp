package com.finapp.backend.security.dto;

import com.finapp.backend.utils.HttpResponse;
import com.finapp.backend.user.dto.UserDto;
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
