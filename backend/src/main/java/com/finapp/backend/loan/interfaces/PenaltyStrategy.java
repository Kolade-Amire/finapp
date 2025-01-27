package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.LoanPaymentSchedule;
import com.finapp.backend.loan.entity.PenaltyConfig;

import java.math.BigDecimal;

public interface PenaltyStrategy {
    BigDecimal calculatePenalty(LoanPaymentSchedule loanPaymentSchedule, PenaltyConfig penaltyConfig);
}
