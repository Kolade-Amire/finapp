package com.finapp.backend.loan.service;


import com.finapp.backend.loan.entity.LoanRepaymentInformation;
import com.finapp.backend.loan.entity.RepaymentFrequency;

public class LoanInformationCalculator {

    /*
    Simple Interest Calculation:
    Total Interest = Principal × Rate (per period) × Tenure (in months)

    Total Repayment:
    Total Repayment = Principal + Total Interest

    Monthly/Weekly Repayment:
    Repayment per Period = Total Repayment/Number of Periods

     */
    public static LoanRepaymentInformation calculateMonthlyInstallment(double principal, double monthlyRate, int tenureInMonths) {
        double totalInterest = principal * tenureInMonths * monthlyRate;

        double totalRepayment = principal + totalInterest;

        double repaymentPerPeriod = totalRepayment / tenureInMonths;

        return LoanRepaymentInformation.builder()
                .rate(monthlyRate)
                .repaymentPerPeriod(repaymentPerPeriod)
                .repaymentFrequency(RepaymentFrequency.MONTHLY)
                .totalInterest(totalInterest)
                .totalRepayment(totalRepayment)
                .build();
    }
}
