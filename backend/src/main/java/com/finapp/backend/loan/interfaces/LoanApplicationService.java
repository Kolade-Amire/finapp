package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.dto.LoanApplicationDto;
import com.finapp.backend.loan.dto.LoanApplicationRequest;

public interface LoanApplicationService {
    LoanApplicationDto applyForLoan(LoanApplicationRequest request);
}
