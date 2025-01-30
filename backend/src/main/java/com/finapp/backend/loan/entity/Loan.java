package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.DisbursementStatus;
import com.finapp.backend.loan.enums.InstallmentFrequency;
import com.finapp.backend.loan.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder
@Table(name = "loans")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID borrowerId;

    @Column(nullable = false)
    private BigDecimal loanPrincipal;

    @Column(nullable = false)
    private BigDecimal totalRepayment;

    @Column(nullable = false)
    private int tenureInMonths;

    @Column(nullable = false)
    private BigDecimal installmentPerPeriod;

    @Column(nullable = false)
    private InstallmentFrequency installmentFrequency;

    @Column(nullable = false)
    private BigDecimal totalInterest;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false)
    private LoanStatus loanStatus;

    @Column(nullable = false)
    private DisbursementStatus disbursementStatus;

    private BigDecimal remainingBalance;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<LoanInstallment> paymentSchedule;

    @Embedded
    private PenaltyConfig penaltyConfig;

    private BigDecimal totalPenaltyAccrued;

    private LocalDate lastPenaltyDate;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        lastUpdatedAt = createdAt;
    }
    @PreUpdate
    protected void onUpdated(){
        lastUpdatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Loan loan = (Loan) o;
        return getId() != null && Objects.equals(getId(), loan.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
