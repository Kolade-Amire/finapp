package com.finapp.backend.loan.entity;

public record LoanValidationResult(boolean isValid, String message) {

    public static LoanValidationResult valid() {
        return new LoanValidationResult(true, "Validation successfully passed for loan application.");
    }

    public static LoanValidationResult invalid(String message) {
        return new LoanValidationResult(false, message);
    }
}
