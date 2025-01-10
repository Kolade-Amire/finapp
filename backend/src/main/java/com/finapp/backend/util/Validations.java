package com.finapp.backend.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Validations {

    //Annotation for validating email
    @Constraint(validatedBy = EmailValidator.class)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidEmail {
        String message() default "Invalid email format";
        Class<?>[] groups() default{};
        Class<? extends Payload>[] payload() default {};
    }

    // Annotation for validating phone numbers
    @Constraint(validatedBy = PhoneNumberValidator.class)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidPhoneNumber {
        String message() default "Invalid phone number format";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    // Annotation for validating password strength
    @Constraint(validatedBy = PasswordValidator.class)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StrongPassword {
        String message() default "Password must meet strength requirements";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

}
