package com.finapp.backend.loan.repository;

import com.finapp.backend.loan.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, UUID> {

    Optional<List<LoanInstallment>> findLoanInstallmentByGracePeriodEndDateBefore(LocalDate date);

}
