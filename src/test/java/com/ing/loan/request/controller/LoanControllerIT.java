package com.ing.loan.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.loan.request.dto.LoanRequest;
import com.ing.loan.request.dto.LoanResponse;
import com.ing.loan.request.services.LoanRequestService;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc
class LoanControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanRequestService loanRequestService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateLoanRequest() throws Exception {
        var request = LoanRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123456789L)
                .build();

        var response = LoanResponse.builder()
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123456789L)
                .build();

        when(loanRequestService.createLoan(org.mockito.ArgumentMatchers.any(LoanRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testGetAllLoans() throws Exception {
        LoanResponse l1 = LoanResponse.builder()
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123346789L)
                .build();

        LoanResponse l2 = LoanResponse.builder()
                .amount(BigDecimal.valueOf(90000))
                .customerFullName("Key Peel")
                .customerId(123453489L)
                .build();

        when(loanRequestService.getAllLoans()).thenReturn(List.of(l1, l2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan-service/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(l1, l2))));
    }

    @Test
    void testGetLoansByLoanId() throws Exception {
        LoanResponse l1 = LoanResponse.builder()
                .id(123348989L)
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(903346789L)
                .build();

        when(loanRequestService.getLoanByLoanId(123348989L)).thenReturn(l1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan-service/loans/123348989"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(l1)));
    }

    @Test
    void testGetLoansByCustomerId() throws Exception {
        LoanResponse l1 = LoanResponse.builder()
                .id(223348989L)
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123346789L)
                .build();

        when(loanRequestService.getLoansByCustomerId(123348989L)).thenReturn(List.of(l1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan-service/customers/customerId/123346789"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(l1)));
    }

    @Test
    void testGetTotalLoanByCustomerId() throws Exception {
        Long customerId = 123456789L;
        BigDecimal expectedTotalLoanAmount = BigDecimal.valueOf(5000);

        when(loanRequestService.getLoanAmountByCustomerId(customerId)).thenReturn(expectedTotalLoanAmount);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan-service/loans/customers/123456789/total", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedTotalLoanAmount.toString()));
    }

    @Test
    void testUpdateLoan() throws Exception {
        LoanResponse l1 = LoanResponse.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .customerFullName("John Doe")
                .customerId(123346789L)
                .build();

        LoanResponse l2 = LoanResponse.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(90000))
                .customerFullName("Key Peel")
                .customerId(123453489L)
                .build();
        when(loanRequestService.updateLoan(1L, ArgumentMatchers.any(LoanRequest.class))).thenReturn(l2);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/loan-service/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(l1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(l2)));
    }

    @Test
    void testDeleteLoan() throws Exception {
        doNothing().when(loanRequestService).deleteLoan(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/loan-service/loans/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}

