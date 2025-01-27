package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.PenaltyCalculationStrategy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Builder
@Table(name = "penalty_config", indexes = {
        @Index(name = "index_loan_id", columnList = "loan_id")
})
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyConfig {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "loan_id", nullable = false)
    private UUID loanId;

    @Column(name = "grace_period_days", nullable = false)
    private int gracePeriodDays;

    @Column(name = "calculation_strategy", nullable = false)
    private PenaltyCalculationStrategy calculationStrategy;

    @Column(name = "penalty_rate", nullable = false)
    private double penaltyRate;
}
