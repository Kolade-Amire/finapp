package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.LoanInstallment;

import java.time.LocalDate;
import java.util.List;

public interface LoanInstallmentManagementService {

    List<LoanInstallment> retrieveOverduePayments(LocalDate date);
}
