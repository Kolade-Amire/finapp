package com.finapp.backend.user.entity;

import com.finapp.backend.user.enums.KYCDocumentType;
import com.finapp.backend.user.enums.KYCLevel;
import com.finapp.backend.user.enums.KYCStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "kyc_profiles")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KYCProfile {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private KYCLevel currentLevel;

    @OneToMany(mappedBy = "kycProfile")
    private List<KYCVerification> kycVerificationList;

    @Enumerated(EnumType.STRING)
    private KYCStatus status;

    private LocalDateTime lastVerifiedAt;

    private LocalDateTime expiresAt;

    @ElementCollection
    @CollectionTable(name = "kyc_supported_documents")
    private Set<KYCDocumentType> supportedDocuments;
}
