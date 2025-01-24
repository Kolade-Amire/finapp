package com.finapp.backend.loan.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRepaymentInformation {
    private double totalInterest;
    private double rate;
    private RepaymentFrequency repaymentFrequency;
    private double totalRepayment;
    private double repaymentPerPeriod;
}
