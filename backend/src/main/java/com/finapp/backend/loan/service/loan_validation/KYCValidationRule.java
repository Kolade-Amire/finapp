package com.finapp.backend.loan.service.loan_validation;

import com.finapp.backend.loan.dto.LoanApplicationRequest;
import com.finapp.backend.loan.entity.LoanValidationResult;
import com.finapp.backend.loan.interfaces.LoanValidationRule;
import com.finapp.backend.user.entity.User;

public class KYCValidationRule implements LoanValidationRule {

    @Override
    public LoanValidationResult validate(LoanApplicationRequest request, User borrower) {
        return null;
    }
}
