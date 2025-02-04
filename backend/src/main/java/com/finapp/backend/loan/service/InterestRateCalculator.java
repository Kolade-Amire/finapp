package com.finapp.backend.loan.service;

import com.finapp.backend.loan.config.LoanConfigProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InterestRateCalculator {

    private final LoanConfigProperties loanConfigProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(InterestRateCalculator.class);

    /*
     Rate = Base Rate ± (Loan Amount Adjustment) ± (Tenure Adjustment)
    */
    private double calculateMonthlyRate(BigDecimal loanAmount, int tenureInMonths) {
        double rate = loanConfigProperties.getMonthlyBaseRate();
        LOGGER.info("Starting interest rate calculation with base rate: {}", rate);

        if (loanAmount.compareTo(new BigDecimal(2_500_000)) >= 0 ) {
            rate -= 1.25;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -1.25 ");
        } else if (loanAmount.compareTo(new BigDecimal(500_000)) >= 0 ) {
            rate -= 0.75;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -0.75 ");
        } else if (loanAmount.compareTo(new BigDecimal(250_000)) >= 0) {
            rate -= 0.45;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -0.45 ");
        } else if (loanAmount.compareTo(new BigDecimal(15_000)) <= 0) {
            rate += 1.25;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +1.25 ");
        } else if (loanAmount.compareTo(new BigDecimal(50_000)) <= 0) {
            rate += 0.75;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +0.75 ");
        } else if (loanAmount.compareTo(new BigDecimal(100_000)) <= 0) {
            rate += 0.45;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +0.45 ");
        }

        if (tenureInMonths >= 10) {
            rate += 1.25;
            LOGGER.info("Tenure -> Loan rate adjustment: +1.25 ");
        } else if (tenureInMonths >= 6) {
            rate += 0.75;
            LOGGER.info("Tenure -> Loan rate adjustment: +0.75 ");
        } else if (tenureInMonths >= 3) {
            rate += 0.45;
            LOGGER.info("Tenure -> Loan rate adjustment: +0.45 ");
        } else if (tenureInMonths >= 1) {
            rate += 0.25;
            LOGGER.info("Tenure -> Loan rate adjustment: +0.25 ");
        }

        rate = (Math.max(rate, 0.0));

        LOGGER.info("Final calculated interest rate: {}", rate);
        return rate;

    }
}
