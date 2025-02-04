package com.finapp.backend.kyc.enums;

public enum KYCVerificationEvent {
    DOCUMENTS_REQUIRED,
    DOCUMENTS_UPLOADED,      // User uploaded documents
    AUTOMATION_STARTED,      // Automated checks started
    AUTOMATION_COMPLETED,    // Automated checks finished
    MANUAL_REVIEW_REQUESTED, // Flagged for manual review
    MANUAL_REVIEW_COMPLETED, // Manual review finished
    APPROVAL,                // Verification approved
    REJECTION,               // Verification rejected
    SUSPEND                  // Verification suspended
}
