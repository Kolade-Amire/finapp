package com.finapp.backend.kyc.dto;

import com.finapp.backend.kyc.enums.KYCDocumentType;
import com.finapp.backend.kyc.enums.KYCLevel;
import com.finapp.backend.kyc.enums.KYCProfileStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KYCProfileDto {
    private String id;
    private String userId;
    private KYCLevel currentLevel;
    private KYCProfileStatus status;
    private LocalDateTime lastVerifiedAt;
    private LocalDate expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private Set<KYCDocumentType> supportedDocuments;
}
