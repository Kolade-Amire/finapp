package com.finapp.backend.kyc.dto;

import com.finapp.backend.kyc.enums.KYCLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class KYCVerificationRequest {
    private String userId;
    private KYCLevel targetLevel;
}
