package com.finapp.backend.user.dto;

import com.finapp.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class UserDtoMapper {

    public static UserDto mapUserToDto(User user) {
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
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static Page<UserDto> mapToPageOfDto(Page<User> users){
        var usersList = users.getContent().stream()
                .map(UserDtoMapper::mapUserToDto)
                .toList();
        return new PageImpl<>(usersList, users.getPageable(), users.getTotalElements());
    }


}
