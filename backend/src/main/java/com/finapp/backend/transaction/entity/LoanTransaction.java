package com.finapp.backend.transaction.entity;

import com.finapp.backend.transaction.enums.LoanTransactionType;
import com.finapp.backend.transaction.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table(name = "transactions")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LoanTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID loanId;

    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanTransactionType type;

    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
