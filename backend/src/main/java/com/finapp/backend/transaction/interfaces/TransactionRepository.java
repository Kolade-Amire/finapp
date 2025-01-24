package com.finapp.backend.transaction.interfaces;

import com.finapp.backend.transaction.entity.LoanTransaction;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends ListCrudRepository<LoanTransaction, UUID> {
}
