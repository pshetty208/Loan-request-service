package com.ing.loan.request.dto;

import com.ing.loan.request.services.utils.Amount;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    @NotNull(message = "Customer Id cannot be empty")
    private Long customerId;

    @NotNull(message = "Amount cannot be empty")
    @DecimalMin(value = Amount.LOAN_MIN, message = "Amount must be greater than or equal to " + Amount.LOAN_MIN)
    @DecimalMax(value = Amount.LOAN_MAX, message = "Amount must be less than or equal to " + Amount.LOAN_MAX)
    private BigDecimal amount;

    @NotBlank(message = "Customer Name cannot be empty")
    private String customerFullName;

    private String loanType;

    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer termMonths;

    private String status;

}
