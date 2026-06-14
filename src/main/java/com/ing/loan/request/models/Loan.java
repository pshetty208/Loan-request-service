package com.ing.loan.request.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "loan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @SequenceGenerator( name = "id_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "id_generator")
    private Long id;

    private BigDecimal amount;

    private String customerFullName;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    private String loanType;

    private Integer termMonths;

    private String status;

    private Instant createdAt;

}
