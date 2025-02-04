package com.finapp.backend.kyc.service;

import com.finapp.backend.kyc.dto.KYCDocumentUploadRequest;
import com.finapp.backend.kyc.dto.KYCProfileRequest;
import com.finapp.backend.kyc.dto.KYCVerificationRequest;
import com.finapp.backend.kyc.entity.KYCProfile;
import com.finapp.backend.kyc.entity.KYCVerification;
import com.finapp.backend.user.entity.User;
import com.finapp.backend.kyc.enums.KYCDocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface KYCService {


    void saveProfile(KYCProfile kycProfile);

    void createNewProfile(User user);

    void deleteProfile(KYCProfile kycProfile);

    KYCProfile getProfileByUserId(UUID userId);

    Set<KYCDocumentType> initiateVerification(KYCVerificationRequest request);

    void uploadDocuments(String userId, List<KYCDocumentUploadRequest> metadata, MultipartFile[] documents);

    void updateKYCProfile(KYCProfileRequest request);

    void processVerification(KYCVerificationRequest request);

    KYCVerification getCurrentVerificationByProfileId(UUID id);

    List<KYCVerification> getAllKYCVerificationsByProfileId(UUID id);

}
