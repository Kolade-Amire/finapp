package com.finapp.backend.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<Validations.ValidPhoneNumber, String> {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "NG");
            return phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
