package com.finapp.backend.loan.service;

import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.entity.PenaltyConfig;
import com.finapp.backend.loan.enums.LoanPaymentStatus;
import com.finapp.backend.loan.interfaces.LoanInstallmentManagementService;
import com.finapp.backend.loan.interfaces.LoanService;
import com.finapp.backend.loan.interfaces.PenaltyStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanInstallmentPenaltyScheduler {

    private final Map<String, PenaltyStrategy> penaltyStrategyMap;
    private final LoanInstallmentManagementService loanInstallmentManagementService;
    private final LoanService loanService;
    private final LocalDate today = LocalDate.now(ZoneOffset.UTC);

    // 11pm UTC / 12 A.M Nigerian time daily
    @Scheduled(cron = "0 0 23 * * ?", zone = "UTC")
    public void calculateDailyPenalties() {
        List<LoanInstallment> overdueInstallments = loanInstallmentManagementService.retrieveOverduePayments(today);

        //update loans with penalties
        overdueInstallments.forEach(loanInstallment -> {
                    Loan loan = loanInstallment.getLoan();
                    BigDecimal maxPenalty = returnMaxPenalty(loan.getLoanPrincipal());
                    BigDecimal currentTotalPenaltyAccrued = loan.getTotalPenaltyAccrued();
                    long daysOverdue = calculateDaysOverdue(loanInstallment.getDueDate());

                    loanInstallment.setDaysOverdue(daysOverdue);

                    // **Skip penalty calculation for current loan if the limit is already reached**
                    if (currentTotalPenaltyAccrued.compareTo(maxPenalty) >= 0) {
                        loan.setTotalPenaltyAccrued(maxPenalty);
                        return;
                    }

                    // Get penalty calculation strategy
                    PenaltyConfig penaltyConfig = loan.getPenaltyConfig();
                    PenaltyStrategy strategy = penaltyStrategyMap.get(penaltyConfig.getCalculationStrategy().toString());

                    //calculate penalty
                    BigDecimal penalty = strategy.calculatePenalty(loanInstallment);
                    BigDecimal newTotalPenalty = currentTotalPenaltyAccrued.add(penalty);

                    //checks if total penalty accrued after adding penalty is greater than limit
                    if (newTotalPenalty.compareTo(maxPenalty) > 0) {
                        //if true set total penalty accrued to limit
                        loan.setTotalPenaltyAccrued(maxPenalty);
                    } else {
                        loan.setTotalPenaltyAccrued(newTotalPenalty);
                    }

                    loan.setLastPenaltyDate(today);
                    loanInstallment.setPaymentStatus(LoanPaymentStatus.OVERDUE);
                    loanService.saveLoan(loan);
                }
        );


        // TODO: Create audit record, update other related entities
    }

    protected BigDecimal returnMaxPenalty(BigDecimal principal) {
        return principal.multiply(BigDecimal.valueOf(0.3));
    }

    protected long calculateDaysOverdue(LocalDate dueDate){
        return ChronoUnit.DAYS.between(dueDate, today);
    }

}
