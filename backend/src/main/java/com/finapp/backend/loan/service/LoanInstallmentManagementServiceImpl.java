package com.finapp.backend.loan.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.interfaces.LoanInstallmentManagementService;
import com.finapp.backend.loan.interfaces.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanInstallmentManagementServiceImpl implements LoanInstallmentManagementService {
    private final LoanInstallmentRepository repository;

    @Override
    public List<LoanInstallment> retrieveOverduePayments(LocalDate date) {
        return repository.findLoanInstallmentByGracePeriodEndDateBefore(date).orElseThrow(
                () -> new CustomFinAppException("No overdue loan payments in the records.")
        );
    }

}
