package com.finapp.backend.loan.service;

import com.finapp.backend.loan.dto.LoanApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*Validate Loan Request based on:
    1. Amount
    2. KYC Level
    3. Outstanding Loan
    4. Has ever defaulted
*
* */

@Component
@RequiredArgsConstructor
public class LoanApplicationValidator {

    public boolean validateLoanRequest(LoanApplicationRequest request){

    }
}
