package com.finapp.backend.loan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class InterestRateCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterestRateCalculator.class);
    @Value("${loan.base-rate}")
    private static double BASE_RATE;

    /*
    Effective Rate = Base Rate ± (Loan Amount Adjustment) ± (Tenure Adjustment)
    */

    public static double calculateInterestRate(double loanAmount, int tenureInMonths) {
        double rate = BASE_RATE;
        LOGGER.info("Starting interest rate calculation with base rate: {}", BASE_RATE);

        if (loanAmount > 1000000) {
            rate -= 3.5;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -3.5 ");
        } else if (loanAmount > 500000) {
            rate -= 1.5;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -1.5 ");
        } else if (loanAmount > 100000) {
            rate -= 0.75;
            LOGGER.info("Loan Amount -> Loan rate adjustment: -0.75 ");
        } else if (loanAmount < 15000) {
            rate += 5.5;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +5.5 ");
        } else if (loanAmount < 50000) {
            rate += 3.5;
            LOGGER.info("Loan Amount -> Loan rate adjustment: +3.5 ");
        }

        if (tenureInMonths > 12) {
            rate += 25.0;
            LOGGER.info("Tenure -> Loan rate adjustment: +25.0 ");
        } else if (tenureInMonths > 6) {
            rate += 15.0;
            LOGGER.info("Tenure -> Loan rate adjustment: +15.0 ");
        } else if (tenureInMonths > 3) {
            rate += 10.0;
            LOGGER.info("Tenure -> Loan rate adjustment: +10.0 ");
        } else if (tenureInMonths > 1) {
            rate += 5.0;
            LOGGER.info("Tenure -> Loan rate adjustment: +5.0 ");
        }

        rate = (Math.max(rate, 0.0));

        LOGGER.info("Final calculated interest rate: {}", rate);
        return rate;

    }
}
