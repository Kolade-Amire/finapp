package com.finapp.backend.kyc.service;

import com.finapp.backend.exception.DocumentUploadException;
import com.finapp.backend.exception.KYCServiceException;
import com.finapp.backend.exception.KYCVerificationException;
import com.finapp.backend.kyc.dto.KYCDocumentUploadRequest;
import com.finapp.backend.kyc.dto.KYCProfileRequest;
import com.finapp.backend.kyc.dto.KYCVerificationRequest;
import com.finapp.backend.kyc.entity.KYCDocument;
import com.finapp.backend.kyc.enums.*;
import com.finapp.backend.kyc.interfaces.KYCDocumentRepository;
import com.finapp.backend.storage.StorageService;
import com.finapp.backend.kyc.config.KYCDocConfig;
import com.finapp.backend.kyc.entity.KYCProfile;
import com.finapp.backend.kyc.entity.KYCVerification;
import com.finapp.backend.user.entity.User;
import com.finapp.backend.kyc.interfaces.KYCProfileRepository;
import com.finapp.backend.kyc.interfaces.KYCVerificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KYCServiceImpl implements KYCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KYCServiceImpl.class);

    private final KYCVerificationRepository verificationRepository;
    private final KYCProfileRepository profileRepository;
    private final StateMachineFactory<KYCVerificationStatus, KYCVerificationEvent> stateMachineFactory;
    private final KYCStateMachineListener stateMachineListener;
    private final KYCDocConfig kycDocConfig;
    private final StorageService storageService;
    private final KYCDocumentRepository kYCDocumentRepository;

    @Override
    @Transactional
    public void saveProfile(KYCProfile profile) {
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            LOGGER.error("An error occurred while trying to save KYCProfile.", e);
            throw new KYCServiceException("An error occurred while trying to save KYCProfile.");
        }
    }

    @Override
    @Transactional
    public void createNewProfile(User user) {
        KYCProfile newProfile = KYCProfile.builder()
                .currentLevel(KYCLevel.NOT_VERIFIED)
                .user(user)
                .build();
        saveProfile(newProfile);
    }

    @Override
    @Transactional
    public void deleteProfile(KYCProfile kycProfile) {
        try {
            profileRepository.delete(kycProfile);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while trying to delete KYC profile.", e);
            throw new KYCServiceException("An unexpected error occurred while trying to delete KYC profile.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public KYCProfile getProfileByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("KYC profile for user with id " + userId.toString() + " not found"));

    }


    @Override
    @Transactional
    public Set<KYCDocumentType> initiateVerification(KYCVerificationRequest request) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            KYCProfile profile = getProfileByUserId(userId);

            //verify if user is eligible for target KYC level
            validateUpgradeEligibility(profile, request.getTargetLevel());

            //trigger verification state machine
            sendVerificationEventAndSubscribe(KYCVerificationEvent.DOCUMENTS_REQUIRED);

            return kycDocConfig.getRequiredDocuments().get(request.getTargetLevel());

        } catch (EntityNotFoundException e) {
            LOGGER.error("KYC profile not found: ", e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (KYCVerificationException e) {
            LOGGER.error("User with ID {} not eligible for this KYC level", request.getUserId());
            throw new KYCVerificationException(e.getLocalizedMessage());
        } catch (Exception e) {
            LOGGER.error("An error occurred during KYC verification: ", e);
            throw new KYCServiceException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void uploadDocuments(String userId, List<KYCDocumentUploadRequest> metadata, MultipartFile[] documents) {
        //validate document and target level match
        if (!isDocTypesMatchForLevel(metadata)) {
            throw new KYCVerificationException("Document type(s) is not valid for target KYC level.");
        }

        KYCProfile kycProfile = getProfileByUserId(UUID.fromString(userId));

        //create KYC Verification entity and persist it
        KYCVerification verification = KYCVerification.builder()
                .kycProfile(kycProfile)
                .requestedLevel(metadata.get(0).getTargetLevel())
                .status(KYCVerificationStatus.DOCUMENTS_SUBMITTED)
                .submittedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        var savedVerification = verificationRepository.save(verification);

        //get storage paths and create document entities for persistence
        List<CompletableFuture<KYCDocument>> uploadFutures = new ArrayList<>();

        for (int i = 0; i < documents.length; i++) {
            final int index = i;
            CompletableFuture<KYCDocument> future;
            try {
                //upload each file to cloud storage
                future = storageService.uploadFile(documents[i])
                        .handle((storagePath, throwable) -> {
                            if (throwable != null) {
                                LOGGER.error("Failed to upload file: {}", throwable.getMessage());
                                throw new DocumentUploadException("Failed to upload file(s)");
                            }
                            var newDocument = KYCDocument.builder()
                                    .type(metadata.get(index).getType())
                                    .documentId(metadata.get(index).getDocumentId())
                                    .storagePath(storagePath)
                                    .verification(savedVerification)
                                    .build();

                            verification.getDocuments().add(newDocument);
                            return newDocument;
                        });
            } catch (Exception e) {
                LOGGER.error("An error occurred during document(s) upload for KYC verification with ID {}", verification.getId(), e);
                throw new DocumentUploadException(e.getLocalizedMessage());
            }
            //updates the upload futures on each document upload call
            uploadFutures.add(future);
        }

        //waits for all uploads to be completed and persist all document entities
        try {
            CompletableFuture.allOf(uploadFutures.toArray(CompletableFuture[]::new))
                            .thenAccept(future -> uploadFutures.stream()
                                    .map(CompletableFuture::join).forEach(kYCDocumentRepository::save))
                                    .join();

        } catch (CompletionException e) {
            LOGGER.error("An error occurred during document(s) upload for KYC verification with ID {}", verification.getId(), e);
            throw new DocumentUploadException("One or more documents failed to upload");
        }

        //trigger state machine for transition
        sendVerificationEventAndSubscribe(KYCVerificationEvent.DOCUMENTS_UPLOADED);


        kycProfile.setStatus(KYCProfileStatus.UNDER_REVIEW);
        profileRepository.save(kycProfile);


    }

    private void sendVerificationEventAndSubscribe(KYCVerificationEvent event) {

        //start state machine for verification event
        Region<KYCVerificationStatus, KYCVerificationEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.addStateListener(stateMachineListener);
        stateMachine.startReactively().subscribe();

        Mono<Boolean> result = stateMachine.sendEvent(
                Mono.just(MessageBuilder.withPayload(event).build())
        ).hasElements();

        // Subscribe to trigger the event and handle the result (optional)
        result.subscribe(accepted -> {
            if (accepted) {
                LOGGER.info("Event accepted");
            } else {
                LOGGER.error("Event not accepted");
            }
        });
    }

    private boolean isDocTypesMatchForLevel(List<KYCDocumentUploadRequest> requests) {
        for (KYCDocumentUploadRequest request : requests) {
            if (!request.getTargetLevel().containsDocType(request.getType())) {
                return false;
            }
        }
        return true;
    }

    private void validateUpgradeEligibility(KYCProfile profile, KYCLevel target) {

        if (!profile.getCurrentLevel().canUpgradeTo(target)) {
            throw new KYCVerificationException("Cannot skip KYC levels");
        }

        if (!profile.canRequestUpgrade()) {
            throw new KYCVerificationException("Status of current KYC Level prohibits upgrade.");
        }
    }

    @Override
    public void processVerification(KYCVerificationRequest request) {

    }

    @Override
    public void updateKYCProfile(KYCProfileRequest request) {

    }

    @Override
    public KYCVerification getCurrentVerificationByProfileId(UUID id) {
        return null;
    }

    @Override
    public List<KYCVerification> getAllKYCVerificationsByProfileId(UUID id) {
        return List.of();
    }


    private boolean doesUserHaveExistingKYCProfile(User user) {
        return false;
    }
}

