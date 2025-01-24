package com.finapp.backend.loan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class InterestRateCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterestRateCalculator.class);
    @Value("${loan.monthly-base-rate}")
    private static double BASE_RATE;

    /*
    Effective Rate = Base Rate ± (Loan Amount Adjustment) ± (Tenure Adjustment)
    */
    private static double calculateMonthlyRate(double loanAmount, int tenureInMonths) {
        double rate = BASE_RATE;
        LOGGER.info("Starting interest rate calculation with base rate: {}", BASE_RATE);

        if (loanAmount >= 2500000) {
            rate -= 1.25;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -1.25 ");
        } else if (loanAmount >= 500000) {
            rate -= 0.75;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -0.75 ");
        } else if (loanAmount >= 250000) {
            rate -= 0.45;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -0.45 ");
        } else if (loanAmount < 15000) {
            rate += 1.25;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +1.25 ");
        } else if (loanAmount <= 50000) {
            rate += 0.75;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +0.75 ");
        } else if (loanAmount < 100000) {
            rate += 0.45;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +0.45 ");
        }

        if (tenureInMonths >= 10) {
            rate = +1.25;
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
