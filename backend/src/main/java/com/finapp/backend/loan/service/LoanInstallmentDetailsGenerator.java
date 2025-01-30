package com.finapp.backend.loan.service;


import com.finapp.backend.loan.entity.LoanRepaymentInformation;
import com.finapp.backend.loan.enums.InstallmentFrequency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanInstallmentDetailsGenerator {

    /*
    Simple Interest Calculation:
    Total Interest = Principal × Rate (per period) × Tenure (in months)

    Total Repayment:
    Total Repayment = Principal + Total Interest

    Monthly/Weekly Repayment:
    Repayment per Period = Total Repayment/Number of Periods

     */
    public static LoanRepaymentInformation calculateMonthlyInstallment(BigDecimal principal, double monthlyRate, int tenureInMonths) {
        BigDecimal totalInterest = principal.multiply(new BigDecimal(tenureInMonths * monthlyRate))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalRepayment = principal.add(totalInterest)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal repaymentPerPeriod = totalRepayment.divide(new BigDecimal(tenureInMonths), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        return LoanRepaymentInformation.builder()
                .rate(monthlyRate)
                .repaymentPerPeriod(repaymentPerPeriod)
                .installmentFrequency(InstallmentFrequency.MONTHLY)
                .totalInterest(totalInterest)
                .totalRepayment(totalRepayment)
                .build();
    }
}
