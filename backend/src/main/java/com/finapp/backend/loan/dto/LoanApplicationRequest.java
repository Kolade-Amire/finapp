package com.finapp.backend.loan.dto;

import com.finapp.backend.loan.enums.LoanPurpose;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequest {
    private String borrowerId;
    private BigDecimal requestedAmount;
    private int tenureInMonths;
    private LoanPurpose loanPurpose;
    private String occupation;
    private BigDecimal borrowerMonthlyIncome;
    private String employmentStatus;
    private String borrowerNotes;
}
