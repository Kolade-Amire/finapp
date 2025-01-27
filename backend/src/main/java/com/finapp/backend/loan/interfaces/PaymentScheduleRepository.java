package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.LoanPaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<LoanPaymentSchedule, UUID> {

    Optional<List<LoanPaymentSchedule>> findPaymentScheduleByNextDueDateBefore(LocalDate date);

}
