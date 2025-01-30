package com.finapp.backend.loan.entity;

import com.finapp.backend.loan.enums.LoanPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanInstallment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private int sequenceNumber;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private LocalDate gracePeriodEndDate;

    @Column(nullable = false)
    private LoanPaymentStatus paymentStatus;

    private int daysOverdue;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    private BigDecimal penalty = BigDecimal.ZERO;


    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now(ZoneOffset.UTC);
    }
    @PreUpdate
    protected void onUpdated(){
        lastUpdated = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LoanInstallment that = (LoanInstallment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
