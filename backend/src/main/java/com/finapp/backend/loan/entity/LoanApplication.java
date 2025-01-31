package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.LoanApplicationStatus;
import com.finapp.backend.loan.enums.LoanPurpose;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table(name = "loan_applications")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID borrowerId;

    @Column(nullable = false)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private int tenureInMonths;

    @Column(nullable = false)
    private double interestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanPurpose loanPurpose;

    @Column(nullable = false)
    private String occupation;

    @Column(nullable = false)
    private BigDecimal monthlyIncome;

    @Column(nullable = false)
    private String employmentStatus;

    @Column(columnDefinition = "TEXT")
    private String borrowerNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanApplicationStatus status = LoanApplicationStatus.PENDING;

    private UUID assignedLoanOfficerId;

    private LocalDate decisionDate;

    @Column(columnDefinition = "TEXT")
    private String adminRemarks;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
