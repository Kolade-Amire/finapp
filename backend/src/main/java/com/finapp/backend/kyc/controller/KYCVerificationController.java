package com.finapp.backend.kyc.controller;

import com.finapp.backend.kyc.dto.KYCDocumentUploadRequest;
import com.finapp.backend.kyc.dto.KYCVerificationRequest;
import com.finapp.backend.kyc.enums.KYCDocumentType;
import com.finapp.backend.kyc.service.KYCService;
import com.finapp.backend.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(AppConstants.BASE_URL + "/kyc/verify")
@RequiredArgsConstructor
public class KYCVerificationController {

    private final KYCService kycService;


    @PostMapping("/{userId}")
    @PreAuthorize("T(java.util.UUID).fromString(#request.userId) == authentication.principal.getId()")
    public ResponseEntity<Set<KYCDocumentType>> initiateKYCVerification(KYCVerificationRequest request) {
        var documentTypes = kycService.initiateVerification(request);
        return ResponseEntity.accepted().body(documentTypes);
    }

    @PostMapping("{userId}/upload-docs")
    @PreAuthorize("T(java.util.UUID).fromString(#userId) == authentication.principal.getId()")
    public void uploadDocuments(@PathVariable String userId, @RequestPart(name = "metadata") @Valid List<KYCDocumentUploadRequest> metadata, @RequestPart(name = "documents") MultipartFile[] documents) {
        kycService.uploadDocuments(userId, metadata, documents);
    }


}
