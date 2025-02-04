package com.finapp.backend.kyc.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = KYCDocumentTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocumentType {
    String message() default "Invalid document type for KYC level";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
