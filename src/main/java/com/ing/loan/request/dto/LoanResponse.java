package com.ing.loan.request.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {

    private Long id;

    private String customerFullName;

    private Long customerId;

    private BigDecimal amount;

    private String loanType;

    private Integer termMonths;

    private String status;

    private Instant createdAt;

}
