package com.finapp.backend.kyc.entity;

import com.finapp.backend.user.entity.User;
import com.finapp.backend.kyc.enums.KYCDocumentType;
import com.finapp.backend.kyc.enums.KYCLevel;
import com.finapp.backend.kyc.enums.KYCProfileStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private KYCLevel currentLevel;

    @Enumerated(EnumType.STRING)
    private KYCProfileStatus status;

    private LocalDateTime lastVerifiedAt;

    private LocalDate expiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    @ElementCollection
    @CollectionTable(name = "kyc_supported_documents")
    private Set<KYCDocumentType> supportedDocuments;

    public boolean canRequestUpgrade() {
        return status == KYCProfileStatus.APPROVED;
    }

    @PrePersist
    void onCreate(){
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        lastUpdatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate(){
        lastUpdatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
