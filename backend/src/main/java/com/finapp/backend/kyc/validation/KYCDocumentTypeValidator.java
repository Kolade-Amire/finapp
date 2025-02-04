package com.finapp.backend.kyc.validation;

import com.finapp.backend.kyc.enums.KYCDocumentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;

public class KYCDocumentTypeValidator implements ConstraintValidator<ValidDocumentType, KYCDocumentType> {

    @Override
    public boolean isValid(KYCDocumentType value, ConstraintValidatorContext context) {
        return value != null && EnumSet.allOf(KYCDocumentType.class).contains(value);
    }
}
