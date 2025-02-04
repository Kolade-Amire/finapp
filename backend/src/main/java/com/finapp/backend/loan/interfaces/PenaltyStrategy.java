package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.entity.LoanInstallment;

import java.math.BigDecimal;

public interface PenaltyStrategy {
    BigDecimal calculatePenalty(LoanInstallment loanInstallment);
}
