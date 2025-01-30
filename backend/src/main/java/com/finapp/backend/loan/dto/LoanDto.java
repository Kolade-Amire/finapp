package com.finapp.backend.loan.dto;

import com.finapp.backend.loan.enums.DisbursementStatus;
import com.finapp.backend.loan.enums.InstallmentFrequency;
import com.finapp.backend.loan.enums.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class LoanDto {
    private UUID id;
    private UUID borrowerId;;
    private BigDecimal loanPrincipal;
    private int tenureInMonths;
    private DisbursementStatus disbursementStatus;
    private BigDecimal installmentPerPeriod;
    private InstallmentFrequency installmentFrequency;
    private BigDecimal totalInterest;
    private double interestRate;
    private LoanStatus loanStatus;
    private BigDecimal remainingBalance;
    private BigDecimal totalPenaltyAccrued;
    private LocalDate lastPenaltyDate;
    private BigDecimal totalRepayment;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;

}
