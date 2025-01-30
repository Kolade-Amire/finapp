package com.finapp.backend.loan.service;

import com.finapp.backend.loan.interfaces.LoanService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class LoanInstallmentPenaltyScheduler {

    private static  final Logger LOGGER = LoggerFactory.getLogger(LoanInstallmentPenaltyScheduler.class);
    private final LoanService loanService;
    private final LocalDate today = LocalDate.now(ZoneOffset.UTC);

    // 11pm UTC / 12 A.M Nigerian time daily
    @Scheduled(cron = "0 0 23 * * ?", zone = "UTC")
    public void calculateDailyPenalties() {
        try {
            loanService.processOverdueInstallments(today);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred. Penalties was not calculated for date: {}", today);
            LOGGER.error("Error ==> ", e);
        }
    }


}
