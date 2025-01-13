package com.finapp.backend.dto;

import com.finapp.backend.utils.Validations;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
