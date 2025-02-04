package com.finapp.backend.kyc.enums;


import java.util.EnumSet;

public enum KYCDocumentType {
    NO_DOCUMENT,
    // Level 1
    NATIONAL_ID,
    INTERNATIONAL_PASSPORT,
    DRIVERS_LICENSE,

    // Level 2
    UTILITY_BILL,
    BANK_STATEMENT,
    TAX_DOCUMENT,

    // Level 3
    SOURCE_OF_WEALTH,
    PEP_DECLARATION,
    BUSINESS_LICENSE;

    public boolean isValidDocumentType(KYCDocumentType type) {
        return EnumSet.allOf(KYCDocumentType.class).contains(type);
    }
}
