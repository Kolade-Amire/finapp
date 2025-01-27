package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.DisbursementStatus;
import com.finapp.backend.loan.enums.LoanStatus;
import com.finapp.backend.loan.enums.InstallmentFrequency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table(name = "loans")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID borrowerId;

    @Column(nullable = false)
    private BigDecimal loanPrincipal;

    @Column(nullable = false)
    private BigDecimal totalRepayment;

    @Column(nullable = false)
    private int tenureInMonths;

    @Column(nullable = false)
    private BigDecimal installmentPerPeriod;

    @Column(nullable = false)
    private InstallmentFrequency installmentFrequency;

    @Column(nullable = false)
    private BigDecimal totalInterest;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false)
    private LoanStatus loanStatus;

    @Column(nullable = false)
    private DisbursementStatus disbursementStatus;

    private BigDecimal remainingBalance;

    private BigDecimal totalPenaltyAmount;

    private LocalDate lastPenaltyDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDate startDate;

    private LocalDate endDate;
}
