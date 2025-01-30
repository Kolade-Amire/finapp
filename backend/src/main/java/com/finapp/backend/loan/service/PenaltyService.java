package com.finapp.backend.loan.service;

import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.entity.PenaltyConfig;
import com.finapp.backend.loan.interfaces.LoanInstallmentManagementService;
import com.finapp.backend.loan.interfaces.LoanService;
import com.finapp.backend.loan.interfaces.PenaltyConfigRepository;
import com.finapp.backend.loan.interfaces.PenaltyStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final Map<String, PenaltyStrategy> penaltyStrategyMap;
    private final LoanInstallmentManagementService loanInstallmentManagementService;
    private final PenaltyConfigRepository penaltyConfigRepository;
    private final LoanService loanService;

    @Scheduled(cron = "0 0 2 * * ?", zone = "UTC") // 8 A.M daily
    public void calculateDailyPenalties() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        List<LoanInstallment> overdueInstallments = loanInstallmentManagementService.retrieveOverduePayments(today);

        //update loans with penalties
        overdueInstallments.forEach(loanInstallment -> {

                    Loan loan = loanInstallment.getLoan();
                    PenaltyConfig penaltyConfig = loan.getPenaltyConfig();
                    PenaltyStrategy strategy = penaltyStrategyMap.get(penaltyConfig.getCalculationStrategy().toString());

                    BigDecimal penalty = strategy.calculatePenalty(loanInstallment, loan);
                    loan.setTotalPenaltyAccrued(loan.getTotalPenaltyAccrued().add(penalty));
                    loan.setLastPenaltyDate(today);
                    //TODO:create audit record, update other related entities || fix multiple saves issue
                    loanService.saveLoan(loan);
                }
        );

    }

}
