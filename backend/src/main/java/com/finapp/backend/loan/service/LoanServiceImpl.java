package com.finapp.backend.loan.service;

import com.finapp.backend.exception.CustomFinAppException;
import com.finapp.backend.exception.SaveEntityException;
import com.finapp.backend.loan.dto.LoanDto;
import com.finapp.backend.loan.dto.LoanMapper;
import com.finapp.backend.loan.dto.LoanUpdateDto;
import com.finapp.backend.loan.entity.Loan;
import com.finapp.backend.loan.entity.LoanInstallment;
import com.finapp.backend.loan.entity.PenaltyConfig;
import com.finapp.backend.loan.enums.LoanPaymentStatus;
import com.finapp.backend.loan.interfaces.LoanInstallmentManagementService;
import com.finapp.backend.loan.interfaces.LoanRepository;
import com.finapp.backend.loan.interfaces.LoanService;
import com.finapp.backend.loan.interfaces.PenaltyStrategy;
import com.finapp.backend.utils.AppConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;
    private final LoanInstallmentManagementService loanInstallmentManagementService;
    private final Map<String, PenaltyStrategy> penaltyStrategyMap;


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

    @Override
    @Transactional
    public void processOverdueInstallments(LocalDate today) {
        List<LoanInstallment> overdueInstallments = loanInstallmentManagementService.retrieveOverduePayments(today);

        //update loans with penalties
        overdueInstallments.forEach(loanInstallment -> {
                    Loan loan = loanInstallment.getLoan();
                    BigDecimal maxPenalty = returnMaxPenalty(loan.getLoanPrincipal());
                    BigDecimal currentTotalPenaltyAccrued = loan.getTotalPenaltyAccrued();
                    long daysOverdue = calculateDaysOverdue(loanInstallment.getDueDate(), today);

                    loanInstallment.setDaysOverdue(daysOverdue);

                    // **Skip penalty calculation for current loan if the limit is already reached**
                    if (currentTotalPenaltyAccrued.compareTo(maxPenalty) >= 0) {
                        loan.setTotalPenaltyAccrued(maxPenalty);
                        loanInstallment.setNotes(loanInstallment.getNotes() + "\n" + today.toString() + ": Penalty has reached penalty limit. So penalty calculation is halted. Check loan details for information on total accrued penalty.");
                        return;
                    }

                    // Get penalty calculation strategy
                    PenaltyConfig penaltyConfig = loan.getPenaltyConfig();
                    PenaltyStrategy strategy = penaltyStrategyMap.get(penaltyConfig.getCalculationStrategy().toString());

                    //calculate penalty
                    BigDecimal penalty = strategy.calculatePenalty(loanInstallment);
                    BigDecimal newTotalPenalty = currentTotalPenaltyAccrued.add(penalty);

                    //checks if total penalty accrued after adding penalty is greater than limit
                    if (newTotalPenalty.compareTo(maxPenalty) > 0) {
                        //if true set total penalty accrued to limit
                        loanInstallment.setPenalty(newTotalPenalty);
                        loanInstallment.setNotes(loanInstallment.getNotes() + "\n" + today.toString() + ": Penalty for today is calculated to be " + newTotalPenalty + "but penalty has exceeded penalty limit. Check loan details for updated total accrued penalty.");
                        loan.setTotalPenaltyAccrued(maxPenalty);
                    } else {
                        loan.setTotalPenaltyAccrued(newTotalPenalty);
                        loanInstallment.setPenalty(newTotalPenalty);
                    }

                    loan.setLastPenaltyDate(today);
                    loanInstallment.setPaymentStatus(LoanPaymentStatus.OVERDUE);
                    saveLoan(loan);
                }
        );

        loanInstallmentManagementService.batchSave(overdueInstallments);
    }

    protected BigDecimal returnMaxPenalty(BigDecimal principal) {
        return principal.multiply(BigDecimal.valueOf(0.3));
    }

    protected long calculateDaysOverdue(LocalDate dueDate, LocalDate today){
        return ChronoUnit.DAYS.between(dueDate, today);
    }
}
