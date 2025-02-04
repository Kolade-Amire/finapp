package com.finapp.backend.kyc.config;

import com.finapp.backend.kyc.enums.KYCDocumentType;
import com.finapp.backend.kyc.enums.KYCLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

import static com.finapp.backend.kyc.enums.KYCDocumentType.*;

@Getter
@ConfigurationProperties(prefix = "kyc")
public class KYCDocConfig {
    @NotNull
    private final Map<KYCLevel, Set<KYCDocumentType>> requiredDocuments = Map.of(
            KYCLevel.LEVEL_1, Set.of(NATIONAL_ID),
            KYCLevel.LEVEL_2, Set.of(UTILITY_BILL, BANK_STATEMENT),
            KYCLevel.LEVEL_3, Set.of(SOURCE_OF_WEALTH, PEP_DECLARATION)
    );

    @NotNull
    private final Map<KYCDocumentType, Set<String>> supportedMimeTypes = Map.of(
            NATIONAL_ID, Set.of("image/jpeg", "image/png", "application/pdf"),
            INTERNATIONAL_PASSPORT, Set.of("image/jpeg", "application/pdf"),
            DRIVERS_LICENSE, Set.of("image/jpeg", "image/png", "application/pdf"),
            UTILITY_BILL, Set.of("image/jpeg", "application/pdf"),
            BANK_STATEMENT, Set.of("application/pdf"),
            TAX_DOCUMENT, Set.of("application/pdf"),
            SOURCE_OF_WEALTH, Set.of("image/jpeg", "application/pdf"),
            BUSINESS_LICENSE, Set.of("image/jpeg", "application/pdf")
    );
}
