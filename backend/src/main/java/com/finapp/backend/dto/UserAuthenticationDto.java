package com.finapp.backend.dto;

import com.finapp.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
public class UserAuthenticationDto {

    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
