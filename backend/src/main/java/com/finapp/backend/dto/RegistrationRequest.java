package com.finapp.backend.dto;

import com.finapp.backend.util.Validations;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RegistrationRequest {

    private String firstname;

    private String lastname;

    @NotBlank(message = "Email is required!")
    @Validations.ValidEmail(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required!")
    @Validations.ValidPhoneNumber(message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Password is required!")
    @Validations.StrongPassword(message = "Password must be at least 8 characters long, include an uppercase letter, a number, and a special character")
    private String password;

    private String confirmPassword;
}
