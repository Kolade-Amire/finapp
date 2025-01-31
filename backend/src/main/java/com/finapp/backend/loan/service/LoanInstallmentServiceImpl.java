package com.finapp.backend.loan.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.interfaces.LoanInstallmentService;
import com.finapp.backend.loan.interfaces.LoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanInstallmentServiceImpl implements LoanInstallmentService {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoanInstallmentServiceImpl.class);
    private final LoanInstallmentRepository repository;

    @Override
    public List<LoanInstallment> retrieveOverduePayments(LocalDate date) {
        return repository.findLoanInstallmentByGracePeriodEndDateBefore(date).orElseThrow(
                () -> new CustomFinAppException("No overdue loan payments in the records.")
        );
    }

    @Override
    public void batchSave(List<LoanInstallment> installments) {
        try {
            repository.saveAll(installments);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred. System was unable to save all loan installment entities.", e);
        }
    }

}
