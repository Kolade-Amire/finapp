package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.entity.Loan;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRepository extends ListCrudRepository<Loan, UUID> {

}
