package com.ing.loan.request.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "customer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @SequenceGenerator( name = "customer_id_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "customer_id_generator")
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotBlank(message = "Customer name cannot be blank")
    private String customerFullName;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

}
