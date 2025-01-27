package com.finapp.backend.loan.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.loan.dto.LoanDto;
import com.finapp.backend.loan.dto.LoanMapper;
import com.finapp.backend.loan.dto.LoanUpdateDto;
import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.interfaces.LoanRepository;
import com.finapp.backend.loan.interfaces.LoanService;
import com.finapp.backend.utils.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;

    @Override
    @Transactional(readOnly = true)
    public Loan findById(UUID id) {
        return loanRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(AppConstants.LOAN_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDto retrieveLoan(String id) {
        try {
            UUID loanId = UUID.fromString(id);
            Loan loan = findById(loanId);
            return LoanMapper.mapLoanToDto(loan);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CustomFinAppException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public LoanDto updateLoan(String loanId, LoanUpdateDto newDetails) {
        try {
            UUID id = UUID.fromString(loanId);
            Loan existingLoan = findById(id);
            LoanMapper.updateLoanFromDto(existingLoan, newDetails);
            return saveLoan(existingLoan);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CustomFinAppException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public LoanDto saveLoan(Loan loan) {
        try {
            Loan savedUser = loanRepository.save(loan);

            return LoanMapper.mapLoanToDto(savedUser);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new SaveEntityException("An error occurred while trying to save loan.");
        }
    }

    @Override
    @Transactional
    public void saveAll(List<Loan> loans) {
        try {
            loanRepository.saveAll(loans);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred. System was unable to save all loan entities.", e);
        }
    }

    @Override
    @Transactional
    public void deleteLoan(String loanId) {
        try {
            UUID id = UUID.fromString(loanId);
            Loan loan = findById(id);
            loanRepository.delete(loan);
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while trying to delete loan.", e);
            throw new CustomFinAppException("An unexpected error occurred while trying to delete loan.");
        }
    }
}
