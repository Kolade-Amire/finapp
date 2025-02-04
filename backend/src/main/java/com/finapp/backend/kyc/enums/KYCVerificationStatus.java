package com.finapp.backend.kyc.enums;

public enum KYCVerificationStatus {
    INITIATED,               // Initial state
    DOCUMENTS_REQUIRED,      // Waiting for user to upload documents
    DOCUMENTS_SUBMITTED,     // User has submitted documents
    AUTOMATION_CHECK_IN_PROGRESS, // Automated checks are running
    MANUAL_REVIEW_REQUIRED,  // Flagged for manual review
    APPROVED,                // Verification successful
    REJECTED,                // Verification failed
    SUSPENDED                // Temporarily suspended (e.g., for investigation)
}
