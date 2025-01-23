package com.finapp.backend.interfaces.repository;

import com.finapp.backend.domain.Transaction;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends ListCrudRepository<Transaction, UUID> {
}
