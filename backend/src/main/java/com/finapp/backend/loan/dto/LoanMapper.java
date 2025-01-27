package com.finapp.backend.loan.dto;

import com.finapp.backend.loan.entity.Loan;

public class LoanMapper {

    public static LoanDto mapLoanToDto(Loan loan){
        return LoanDto.builder()
                .id(loan.getId())
                .borrowerId(loan.getBorrowerId())
                .loanPrincipal(loan.getLoanPrincipal())
                .tenureInMonths(loan.getTenureInMonths())
                .disbursementStatus(loan.getDisbursementStatus())
                .installmentPerPeriod(loan.getInstallmentPerPeriod())
                .installmentFrequency(loan.getInstallmentFrequency())
                .totalInterest(loan.getTotalInterest())
                .interestRate(loan.getInterestRate())
                .loanStatus(loan.getLoanStatus())
                .remainingBalance(loan.getRemainingBalance())
                .totalPenaltyAmount(loan.getTotalPenaltyAmount())
                .lastPenaltyDate(loan.getLastPenaltyDate())
                .totalRepayment(loan.getTotalRepayment())
                .createdAt(loan.getCreatedAt())
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate())
                .build();
    }

    public static void updateLoanFromDto(Loan existingLoan, LoanUpdateDto newDetails){
        //update only allowed updates
        if (newDetails.getTenureInMonths() != null) {
            existingLoan.setTenureInMonths(newDetails.getTenureInMonths());
        }
        if (newDetails.getDisbursementStatus() != null) {
            existingLoan.setDisbursementStatus(newDetails.getDisbursementStatus());
        }
        if (newDetails.getInstallmentPerPeriod() != null) {
            existingLoan.setInstallmentPerPeriod(newDetails.getInstallmentPerPeriod());
        }
        if (newDetails.getTotalInterest() != null) {
            existingLoan.setTotalInterest(newDetails.getTotalInterest());
        }
        if (newDetails.getInterestRate() != null) {
            existingLoan.setInterestRate(newDetails.getInterestRate());
        }
        if (newDetails.getLoanStatus() != null) {
            existingLoan.setLoanStatus(newDetails.getLoanStatus());
        }
        if (newDetails.getRemainingBalance() != null) {
            existingLoan.setRemainingBalance(newDetails.getRemainingBalance());
        }
        if (newDetails.getTotalPenaltyAmount() != null) {
            existingLoan.setTotalPenaltyAmount(newDetails.getTotalPenaltyAmount());
        }if (newDetails.getLastPenaltyDate() != null) {
            existingLoan.setLastPenaltyDate(newDetails.getLastPenaltyDate());
        }
        if (newDetails.getLastPenaltyDate() != null) {
            existingLoan.setLastPenaltyDate(newDetails.getLastPenaltyDate());
        }
        if (newDetails.getEndDate() != null) {
            existingLoan.setEndDate(newDetails.getEndDate());
        }
    }
}
