package com.finapp.backend.dto.auth;

import com.finapp.backend.security.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
