package com.finapp.backend.loan.dto;

import com.finapp.backend.loan.enums.DisbursementStatus;
import com.finapp.backend.loan.enums.InstallmentFrequency;
import com.finapp.backend.loan.enums.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class LoanUpdateDto {
    private Integer tenureInMonths;
    private DisbursementStatus disbursementStatus;
    private BigDecimal installmentPerPeriod;
    private InstallmentFrequency installmentFrequency;
    private BigDecimal totalInterest;
    private Double interestRate;
    private LoanStatus loanStatus;
    private BigDecimal remainingBalance;
    private BigDecimal totalPenaltyAmount;
    private LocalDate lastPenaltyDate;
    private BigDecimal totalRepayment;
    private LocalDate endDate;
}
