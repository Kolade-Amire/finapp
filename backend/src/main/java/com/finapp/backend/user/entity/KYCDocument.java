package com.finapp.backend.user.entity;

import com.finapp.backend.user.enums.KYCDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "kyc_documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KYCDocument {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private KYCDocumentType type;

    private LocalDate expiryDate;

    @Column(name = "file_path")
    private String storagePath; // Reference to encrypted document storage

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_id")
    private KYCVerification verification;

    private boolean verifiedBySystem;

    private boolean verifiedByHuman;
}
