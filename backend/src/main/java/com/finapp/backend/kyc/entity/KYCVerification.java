package com.finapp.backend.kyc.entity;

import com.finapp.backend.user.entity.User;
import com.finapp.backend.kyc.enums.KYCLevel;
import com.finapp.backend.kyc.enums.KYCVerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "kyc_verifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KYCVerification {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_profile_id", unique = true, nullable = false)
    private KYCProfile kycProfile;

    @Enumerated(EnumType.STRING)
    private KYCLevel requestedLevel;

    @Enumerated(EnumType.STRING)
    private KYCVerificationStatus status;

    private String rejectionReason;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "verification", orphanRemoval = true)
    private Set<KYCDocument> documents;

    @ManyToOne
    private User reviewedBy; // Admin/Compliance officer
}
