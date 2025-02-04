package com.finapp.backend.loan.config;

import com.finapp.backend.kyc.enums.KYCLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.Map;

import static com.finapp.backend.kyc.enums.KYCLevel.*;

@ConfigurationProperties(prefix = "loan")
@Getter
@Setter
public class LoanConfigProperties {

    @NotNull
    private Map<KYCLevel, BigDecimal> maxLoanAmounts =
            Map.of(
                    LEVEL_0, BigDecimal.valueOf(0),
                    LEVEL_1, BigDecimal.valueOf(50_000),
                    LEVEL_2, BigDecimal.valueOf(120_000),
                    LEVEL_3, BigDecimal.valueOf(550_000)
            );

    private static final double DEFAULT_MONTHLY_BASE_RATE = 7.00;

    @Min(0)
    @Max(100)
    private double monthlyBaseRate = DEFAULT_MONTHLY_BASE_RATE;

    @Positive
    @NotNull
    private BigDecimal minLoanAmount = BigDecimal.valueOf(10_000);

}
