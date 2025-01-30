package com.finapp.backend.loan.service;

import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.interfaces.PenaltyStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("DAILY_ACCRUAL")
public class DailyAccrualStrategy implements PenaltyStrategy {

    // Penalty = Overdue Amount × Penalty Rate
    @Override
    public BigDecimal calculatePenalty(LoanInstallment loanInstallment, Loan loan) {
        return loanInstallment.getLoan()
                .getInstallmentPerPeriod()
                .multiply(
                        BigDecimal.valueOf(
                                loan.getPenaltyConfig().getPenaltyRate()
                        )
                )
                .setScale(2, RoundingMode.HALF_UP);
    }
}
