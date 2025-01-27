package com.finapp.backend.loan.service;

import com.finapp.backend.loan.entity.LoanPaymentSchedule;
import com.finapp.backend.loan.entity.PenaltyConfig;
import com.finapp.backend.loan.interfaces.PenaltyStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("DAILY_ACCRUAL")
public class DailyAccrualStrategy implements PenaltyStrategy {

    // Penalty = Overdue Amount × Penalty Rate
    @Override
    public BigDecimal calculatePenalty(LoanPaymentSchedule loanPaymentSchedule, PenaltyConfig penaltyConfig) {
        return loanPaymentSchedule.getPaymentInstallment().multiply(BigDecimal.valueOf(penaltyConfig.getPenaltyRate()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
