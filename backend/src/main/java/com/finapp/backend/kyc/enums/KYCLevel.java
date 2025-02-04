package com.finapp.backend.kyc.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.finapp.backend.kyc.enums.KYCDocumentType.*;

@Getter
@RequiredArgsConstructor
public enum KYCLevel {
    //unverified new user
    NOT_VERIFIED(null),
    //email/phone number verification
    LEVEL_0(null),
    //valid identity verification
    LEVEL_1(Set.of(NATIONAL_ID, DRIVERS_LICENSE, INTERNATIONAL_PASSPORT)),
    //address verification
    LEVEL_2(Set.of(UTILITY_BILL, BANK_STATEMENT, TAX_DOCUMENT)),
    //"enhanced due diligence
    LEVEL_3(Set.of(SOURCE_OF_WEALTH, PEP_DECLARATION, BUSINESS_LICENSE)),
    //in-office determination
    LEVEL_4(null);

    private final Set<KYCDocumentType> types;

    public boolean containsDocType(KYCDocumentType type) {
        return types.contains(type);
    }

    private static final KYCLevel[] levels = KYCLevel.values();

    public KYCLevel next() {
        return levels[Math.min(this.ordinal() + 1, levels.length - 1)];
    }

    public boolean canUpgradeTo(KYCLevel target) {
        return target.ordinal() == this.ordinal() + 1;
    }
}
