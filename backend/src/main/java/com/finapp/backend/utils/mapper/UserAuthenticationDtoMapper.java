package com.finapp.backend.utils.mapper;

import com.finapp.backend.dto.auth.UserDto;
import com.finapp.backend.domain.User;

public class UserAuthenticationDtoMapper {

    public static UserDto mapUserToUserAuthDto(User user) {
        return UserDto.builder()
                .id(user.getId().toString())
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
