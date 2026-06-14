package com.ing.loan.request.services;

import com.ing.loan.request.dto.LoanRequest;
import com.ing.loan.request.dto.LoanResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.models.Customer;
import com.ing.loan.request.models.Loan;
import com.ing.loan.request.persistence.CustomerRepository;
import com.ing.loan.request.persistence.LoanRepository;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanRequestServiceTest {

    @InjectMocks
    private LoanRequestServiceImpl loanRequestService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateLoanWithValidInput() throws CustomerNotFoundException {
        when(customerRepository.findByCustomerId(anyLong())).thenReturn(Collections.singletonList(Customer.builder()
                .customerId(123456789L)
                .customerFullName("John Doe")
                .id(123456780L)
                .build()));
        when(loanRepository.save(any(Loan.class))).thenReturn(Loan.builder()
                .amount(BigDecimal.valueOf(1000))
                .id(111111111L)
                .customerFullName("John Doe")
                .customer(Customer.builder()
                        .customerId(123456789L)
                        .customerFullName("John Doe")
                        .id(123456780L)
                        .build())
                .build());

        LoanRequest loanRequest = LoanRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123456789L)
                .build();

        LoanResponse loanResponse = loanRequestService.createLoan(loanRequest);

        assertNotNull(loanResponse);
        assertEquals(BigDecimal.valueOf(1000), loanResponse.getAmount());
        assertEquals("John Doe", loanResponse.getCustomerFullName());
        assertEquals(123456789L, loanResponse.getCustomerId());

        verify(customerRepository, times(1)).findByCustomerId(anyLong());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    public void testCreateLoanWithNonExistentCustomer() {
        when(customerRepository.findByCustomerId(anyLong())).thenReturn(Collections.emptyList());

        LoanRequest loanRequest = LoanRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123456789L)
                .build();

        assertThrows(CustomerNotFoundException.class, () -> loanRequestService.createLoan(loanRequest));

        verify(customerRepository, times(1)).findByCustomerId(anyLong());
        verifyNoInteractions(loanRepository);
    }

    @Test
    public void testGetLoanAmountByCustomerIdWithNoLoans() {
        // Mocking loan repository to return an empty list of loans for the given customer ID
        when(loanRepository.sumAmountByCustomerId(123456789L)).thenReturn(BigDecimal.ZERO);

        // Calling method under test
        BigDecimal totalLoanAmount = loanRequestService.getLoanAmountByCustomerId(123456789L);

        // Assertions
        assertEquals(BigDecimal.ZERO, totalLoanAmount);
    }

    @Test
    public void testGetLoanAmountByCustomerIdWithSingleLoan() {
        // Mocking loan repository to return a single loan for the given customer ID
        when(loanRepository.sumAmountByCustomerId(123456789L)).thenReturn(BigDecimal.valueOf(1000));

        BigDecimal totalLoanAmount = loanRequestService.getLoanAmountByCustomerId(123456789L);

        assertEquals(BigDecimal.valueOf(1000), totalLoanAmount);
    }

}
