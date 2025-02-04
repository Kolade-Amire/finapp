package com.finapp.backend.kyc.dto;

import com.finapp.backend.kyc.enums.KYCDocumentType;
import com.finapp.backend.kyc.enums.KYCLevel;
import com.finapp.backend.kyc.validation.ValidDocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class KYCDocumentUploadRequest {
    @NotNull(message = "Document type is required")
    @ValidDocumentType
    private KYCDocumentType type;
    private KYCLevel targetLevel;
    private String documentId;
}