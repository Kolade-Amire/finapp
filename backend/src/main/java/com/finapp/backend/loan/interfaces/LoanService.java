package com.finapp.backend.loan.interfaces;

import com.finapp.backend.loan.dto.LoanDto;
import com.finapp.backend.loan.dto.LoanUpdateDto;
import com.finapp.backend.loan.entity.Loan;

import java.util.List;
import java.util.UUID;

public interface LoanService {

    Loan findById(UUID id);
    LoanDto retrieveLoan(String id);
    LoanDto updateLoan(String id, LoanUpdateDto newDetails);
    LoanDto saveLoan(Loan loan);
    void saveAll(List<Loan> loans);
    void deleteLoan(String id);
}
