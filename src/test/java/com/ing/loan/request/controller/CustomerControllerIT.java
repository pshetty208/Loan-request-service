package com.ing.loan.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.loan.request.dto.CustomerRequest;
import com.ing.loan.request.dto.CustomerResponse;
import com.ing.loan.request.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
public class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void testCreateCustomer() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .customerId(11111L)
                .customerFullName("Jane Doe")
                .build();

        CustomerResponse created = CustomerResponse.builder()
                .customerId(11111L)
                .customerFullName("Jane Doe")
                .build();

        when(customerService.createCustomer(org.mockito.ArgumentMatchers.any(CustomerRequest.class))).thenReturn(created);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customer-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(created)));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        CustomerResponse c1 = CustomerResponse.builder().customerId(111L).customerFullName("A").build();
        CustomerResponse c2 = CustomerResponse.builder().customerId(222L).customerFullName("B").build();

        when(customerService.getAllCustomers()).thenReturn(List.of(c1, c2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer-service"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(c1, c2))));
    }

    @Test
    void testGetByCustomerId() throws Exception {
        CustomerResponse c = CustomerResponse.builder().customerId(111L).customerFullName("A").build();
        when(customerService.getCustomerById(111L)).thenReturn(c);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customer-service/customerId/111"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(c)));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerRequest req = CustomerRequest.builder().customerId(111L).customerFullName("A Updated").build();
        CustomerResponse updated = CustomerResponse.builder().customerId(111L).customerFullName("A Updated").build();
        when(customerService.updateCustomer(eq(1L), org.mockito.ArgumentMatchers.any(CustomerRequest.class))).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer-service/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updated)));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customer-service/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

