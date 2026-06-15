package com.ing.loan.request.services;

import com.ing.loan.request.dto.CustomerRequest;
import com.ing.loan.request.dto.CustomerResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.models.Customer;
import com.ing.loan.request.persistence.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public CustomerResponse createCustomer(CustomerRequest customerRequest) {
		Customer customer = Customer.builder()
				.customerId(customerRequest.getCustomerId())
				.customerFullName(customerRequest.getCustomerFullName())
				.email(customerRequest.getEmail())
				.phoneNumber(customerRequest.getPhoneNumber())
				.address(customerRequest.getAddress())
				.dateOfBirth(customerRequest.getDateOfBirth())
				.build();
		Customer saved = customerRepository.save(customer);
		return toResponse(saved);
	}

	@Override
	public CustomerResponse getCustomerById(Long id) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByCustomerId(id);
		if (customer == null) {
			throw new CustomerNotFoundException("Customer with customerId: " + id + " not found.");
		}
		return toResponse(customer);
	}

	@Override
	public List<CustomerResponse> getAllCustomers() {
		return customerRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public CustomerResponse updateCustomer(Long customerId, CustomerRequest customerRequest) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId);
		if (customer == null) {
			throw new CustomerNotFoundException("Customer with customerId: " + customerId + " not found.");
		}
		customer.setCustomerFullName(customerRequest.getCustomerFullName());
		customer.setEmail(customerRequest.getEmail());
		customer.setPhoneNumber(customerRequest.getPhoneNumber());
		customer.setAddress(customerRequest.getAddress());
		customer.setDateOfBirth(customerRequest.getDateOfBirth());
		Customer saved = customerRepository.save(customer);
		return toResponse(saved);
	}

	@Override
	public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByCustomerId(customerId);
		if (customer == null) {
			throw new CustomerNotFoundException("Customer with customerId: " + customerId + " not found.");
		}
		customerRepository.delete(customer);
	}

	private CustomerResponse toResponse(Customer customer) {
		return CustomerResponse.builder()
				.customerId(customer.getCustomerId())
				.customerFullName(customer.getCustomerFullName())
				.email(customer.getEmail())
				.phoneNumber(customer.getPhoneNumber())
				.address(customer.getAddress())
				.dateOfBirth(customer.getDateOfBirth())
				.build();
	}

}
