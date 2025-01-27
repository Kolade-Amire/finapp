package com.finapp.backend.loan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID borrowerId;

    @Column(nullable = false)
    private BigDecimal loanPrincipal;

    @Column(nullable = false)
    private BigDecimal totalRepayment;

    @Column(nullable = false)
    private int tenure;

    @Column(nullable = false)
    private BigDecimal repaymentPerPeriod;

    @Column(nullable = false)
    private RepaymentFrequency repaymentFrequency;

    @Column(nullable = false)
    private BigDecimal totalInterest;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false)
    private LoanStatus loanStatus;

    @Column(nullable = false)
    private DisbursementStatus disbursementStatus;

    private BigDecimal remainingBalance;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
