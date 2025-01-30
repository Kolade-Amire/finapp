package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.PenaltyCalculationStrategy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PenaltyConfig {

    @Column(nullable = false)
    private int gracePeriodDays;

    @Column(nullable = false)
    private PenaltyCalculationStrategy calculationStrategy;

    @Column(nullable = false)
    private double penaltyRate;

}
