package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.dto.LoanApplicationRequest;
import com.finapp.backend.loan.entity.LoanValidationResult;
import com.finapp.backend.user.entity.User;

public interface LoanValidationRule {
    LoanValidationResult validate(LoanApplicationRequest request, User borrower);
}
