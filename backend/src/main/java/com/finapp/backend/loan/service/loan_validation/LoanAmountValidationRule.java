package com.finapp.backend.loan.service.loan_validation;

import com.finapp.backend.loan.config.LoanConfigProperties;
import com.finapp.backend.loan.dto.LoanApplicationRequest;
import com.finapp.backend.loan.entity.LoanValidationResult;
import com.finapp.backend.loan.interfaces.LoanValidationRule;
import com.finapp.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LoanAmountValidationRule implements LoanValidationRule {
    private final LoanConfigProperties config;

    @Override
    public LoanValidationResult validate(LoanApplicationRequest request, User user) {
        BigDecimal requestedAmount = request.getRequestedAmount();

        if (requestedAmount.compareTo(config.getMinLoanAmount()) < 0) {
            return LoanValidationResult.invalid(
                    "Requested amount is below minimum of " + config.getMinLoanAmount()
            );
        }

        BigDecimal userMaxAmount = config.getMaxLoanAmounts().get(user.getKycLevel());

        if (requestedAmount.compareTo(userMaxAmount) > 0) {
            return LoanValidationResult.invalid(
                   String.format("Requested amount is greater than maximum allowed for user KYC level. Current user KYC level is %s. Maximum loan amount possible is %f", user.getKycLevel(), userMaxAmount)
            );
        }

        return LoanValidationResult.valid();
    }
}