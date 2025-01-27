package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.LoanPaymentStatus;
import com.finapp.backend.loan.enums.InstallmentFrequency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Table(name = "payment_schedule")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoanPaymentSchedule {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "loan_id", nullable = false)
    private UUID loanId;

    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @Column(name = "loan_balance", nullable = false)
    private BigDecimal loanBalance;

    @Column(name = "payment_installment", nullable = false)
    private BigDecimal paymentInstallment;

    @Column(name = "installment_frequency", nullable = false)
    private InstallmentFrequency installmentFrequency;

    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private LoanPaymentStatus paymentStatus;

    @Column(name = "days_overdue")
    private int daysOverdue;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;


}
