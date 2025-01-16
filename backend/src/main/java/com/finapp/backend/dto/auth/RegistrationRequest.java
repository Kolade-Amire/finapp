package com.finapp.backend.dto.auth;

import com.finapp.backend.utils.StrongPassword;
import com.finapp.backend.utils.ValidEmail;
import com.finapp.backend.utils.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"password", "confirmPassword"})
public class RegistrationRequest {

    @NotBlank(message = "Firstname is required.")
    private String firstname;

    @NotBlank(message = "Lastname is required.")
    private String lastname;

    @NotBlank(message = "Email is required.")
    @ValidEmail()
    private String email;

    @NotBlank(message = "Phone number is required.")
    @ValidPhoneNumber()
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @StrongPassword(message = "Password must be at least 8 characters long, include an uppercase letter, a number, and a special character.")
    private String password;

    @NotBlank(message= "You have to confirm password.")
    @StrongPassword(message = "Password must be at least 8 characters long, include an uppercase letter, a number, and a special character.")
    private String confirmPassword;
}
