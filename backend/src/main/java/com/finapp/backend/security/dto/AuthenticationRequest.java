package com.finapp.backend.security.dto;

import com.finapp.backend.utils.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email is required.")
    @ValidEmail(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;
}
