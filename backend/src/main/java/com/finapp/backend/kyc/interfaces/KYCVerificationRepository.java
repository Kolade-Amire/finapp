package com.finapp.backend.kyc.interfaces;

import com.finapp.backend.kyc.entity.KYCVerification;
import com.finapp.backend.kyc.enums.KYCVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KYCVerificationRepository extends JpaRepository<KYCVerification, UUID> {

    Optional<KYCVerification> findByKycProfileId(UUID id);
    Optional<List<KYCVerification>> findByStatus(KYCVerificationStatus status);
}
