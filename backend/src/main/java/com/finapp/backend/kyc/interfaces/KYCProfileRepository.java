package com.finapp.backend.kyc.interfaces;

import com.finapp.backend.kyc.entity.KYCProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KYCProfileRepository extends JpaRepository<KYCProfile, UUID> {

    Optional<KYCProfile> findByUserId(UUID userId);
}
