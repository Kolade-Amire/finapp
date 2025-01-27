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
@Table(name = "penalty_config")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyAudit {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanId", nullable = false)
    private Loan loanId;
    private LocalDateTime calculationDate;
    private BigDecimal penaltyAmount;
    private int overdueDays;
    private String notes;

}
