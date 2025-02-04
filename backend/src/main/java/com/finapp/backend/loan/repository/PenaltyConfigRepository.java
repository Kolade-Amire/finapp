package com.finapp.backend.loan.repository;

import com.finapp.backend.loan.entity.PenaltyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PenaltyConfigRepository extends JpaRepository<PenaltyConfig, UUID> {

    PenaltyConfig findPenaltyConfigByLoanId(UUID loanId);
}
