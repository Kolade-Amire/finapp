package com.finapp.backend.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserUpdateDto {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String profilePictureUrl;
}
