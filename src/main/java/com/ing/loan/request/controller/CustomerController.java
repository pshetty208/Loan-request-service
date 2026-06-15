package com.ing.loan.request.controller;

import com.ing.loan.request.dto.CustomerRequest;
import com.ing.loan.request.dto.CustomerResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-service")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse created = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customerId/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) throws CustomerNotFoundException {
        CustomerResponse customers = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerRequest customerRequest) throws CustomerNotFoundException {
        CustomerResponse customer = customerService.updateCustomer(customerId, customerRequest);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

}