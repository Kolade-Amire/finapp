package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.dto.LoanApplicationDto;

public interface LoanApplicationService {

    LoanApplicationDto applyForLoan(LoanApplicationRequest request);
}
