package com.finapp.backend.utils.mapper;

import com.finapp.backend.dto.UserAuthenticationDto;
import com.finapp.backend.domain.User;

public class UserAuthenticationDtoMapper {

    public static UserAuthenticationDto mapUserToUserAuthDto(User user) {
        return UserAuthenticationDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePictureUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
