package com.finapp.backend.kyc.dto;

import com.finapp.backend.kyc.entity.KYCProfile;

public class KYCProfileMapper {

    public static KYCProfileDto mapProfileToDto(KYCProfile profile){
        return KYCProfileDto.builder()
                .id(profile.getId().toString())
                .userId(profile.getUser().getId().toString())
                .currentLevel(profile.getCurrentLevel())
                .status(profile.getStatus())
                .lastVerifiedAt(profile.getLastVerifiedAt())
                .expiresAt(profile.getExpiresAt())
                .createdAt(profile.getCreatedAt())
                .lastUpdatedAt(profile.getLastUpdatedAt())
                .supportedDocuments(profile.getSupportedDocuments())
                .build();
    }
}
