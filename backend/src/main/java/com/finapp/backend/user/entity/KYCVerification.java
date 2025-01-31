package com.finapp.backend.user.entity;

import com.finapp.backend.user.enums.KYCLevel;
import com.finapp.backend.user.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    private KYCProfile kycProfile;

    @Enumerated(EnumType.STRING)
    private KYCLevel requestedLevel;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private String rejectionReason;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "verification")
    private List<KYCDocument> documents;

    @ManyToOne
    private User reviewedBy; // Admin/Compliance officer
}
