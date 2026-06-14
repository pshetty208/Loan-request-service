package com.ing.loan.request.services;

import com.ing.loan.request.dto.CustomerRequest;
import com.ing.loan.request.dto.CustomerResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.models.Customer;
import com.ing.loan.request.persistence.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(Customer.builder()
                .id(1L)
                .customerId(123L)
                .customerFullName("Jane Doe")
                .email("jane@example.com")
                .phoneNumber("+1234567890")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build());

        CustomerRequest request = CustomerRequest.builder()
                .customerId(123L)
                .customerFullName("Jane Doe")
                .email("jane@example.com")
                .phoneNumber("+1234567890")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        CustomerResponse response = customerService.createCustomer(request);

        assertNotNull(response);
        assertEquals(123L, response.getCustomerId());
        assertEquals("Jane Doe", response.getCustomerFullName());
        assertEquals("jane@example.com", response.getEmail());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetCustomerByIdExists() throws CustomerNotFoundException {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(Customer.builder()
                .id(1L)
                .customerId(123L)
                .customerFullName("Jane Doe")
                .build()));

        CustomerResponse resp = customerService.getCustomerById(1L);

        assertNotNull(resp);
        assertEquals(123L, resp.getCustomerId());
        assertEquals("Jane Doe", resp.getCustomerFullName());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(2L));

        verify(customerRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(
                Customer.builder().id(1L).customerId(1L).customerFullName("A").build(),
                Customer.builder().id(2L).customerId(2L).customerFullName("B").build()
        ));

        var list = customerService.getAllCustomers();

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getCustomerFullName());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCustomer() throws CustomerNotFoundException {
        Customer existing = Customer.builder()
                .id(5L)
                .customerId(555L)
                .customerFullName("Old Name")
                .email("old@example.com")
                .build();

        when(customerRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerRequest update = CustomerRequest.builder()
                .customerId(555L)
                .customerFullName("New Name")
                .email("new@example.com")
                .build();

        CustomerResponse resp = customerService.updateCustomer(5L, update);

        assertNotNull(resp);
        assertEquals("New Name", resp.getCustomerFullName());
        assertEquals("new@example.com", resp.getEmail());

        verify(customerRepository, times(1)).findById(5L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testDeleteCustomer() throws CustomerNotFoundException {
        Customer existing = Customer.builder().id(9L).customerId(999L).customerFullName("ToDelete").build();
        when(customerRepository.findById(9L)).thenReturn(Optional.of(existing));

        customerService.deleteCustomer(9L);

        verify(customerRepository, times(1)).findById(9L);
        verify(customerRepository, times(1)).delete(existing);
    }

    @Test
    public void testDeleteCustomerNotFound() {
        when(customerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(10L));

        verify(customerRepository, times(1)).findById(10L);
        verify(customerRepository, times(0)).delete(any(Customer.class));
    }

}

