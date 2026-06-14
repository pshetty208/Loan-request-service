package com.ing.loan.request.services;

import com.ing.loan.request.dto.CustomerRequest;
import com.ing.loan.request.dto.CustomerResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

	CustomerResponse createCustomer(CustomerRequest customerRequest);

	CustomerResponse getCustomerById(Long id) throws CustomerNotFoundException;

	List<CustomerResponse> getAllCustomers();

	CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) throws CustomerNotFoundException;

	void deleteCustomer(Long id) throws CustomerNotFoundException;

}
