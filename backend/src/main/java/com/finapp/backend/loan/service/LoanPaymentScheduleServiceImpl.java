package com.finapp.backend.loan.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.loan.entity.LoanPaymentSchedule;
import com.finapp.backend.loan.interfaces.LoanPaymentScheduleService;
import com.finapp.backend.loan.interfaces.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanPaymentScheduleServiceImpl implements LoanPaymentScheduleService {
    private final PaymentScheduleRepository repository;

    @Override
    public List<LoanPaymentSchedule> retrieveOverduePayments(LocalDate date) {
        return repository.findPaymentScheduleByNextDueDateBefore(date).orElseThrow(
                () -> new CustomFinAppException("No overdue loan payments in the records.")
        );
    }

}
