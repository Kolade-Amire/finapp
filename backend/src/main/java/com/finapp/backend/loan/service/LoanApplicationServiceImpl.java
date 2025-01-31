package com.finapp.backend.loan.service;

import com.finapp.backend.loan.interfaces.LoanApplicationService;
import com.finapp.backend.loan.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepository;
}
