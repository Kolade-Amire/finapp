package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.LoanPaymentSchedule;

import java.time.LocalDate;
import java.util.List;

public interface LoanPaymentScheduleService {

    List<LoanPaymentSchedule> retrieveOverduePayments(LocalDate date);
}
