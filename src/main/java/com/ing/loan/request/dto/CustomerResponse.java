package com.ing.loan.request.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long customerId;

    private String customerFullName;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

}

