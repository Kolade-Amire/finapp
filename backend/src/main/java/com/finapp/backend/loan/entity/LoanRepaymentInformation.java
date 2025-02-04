package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.InstallmentFrequency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRepaymentInformation {
    private BigDecimal totalInterest;
    private double rate;
    private InstallmentFrequency installmentFrequency;
    private BigDecimal totalRepayment;
    private BigDecimal repaymentPerPeriod;
}
