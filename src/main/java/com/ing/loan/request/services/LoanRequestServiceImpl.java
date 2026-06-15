package com.ing.loan.request.services;

import com.ing.loan.request.dto.LoanRequest;
import com.ing.loan.request.dto.LoanResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.exception.LoanNotFoundException;
import com.ing.loan.request.models.Customer;
import com.ing.loan.request.models.Loan;
import com.ing.loan.request.persistence.CustomerRepository;
import com.ing.loan.request.persistence.LoanRepository;
import com.ing.loan.request.services.utils.LoanStatus;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoanRequestServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public LoanResponse createLoan(LoanRequest loanRequest) throws CustomerNotFoundException {

        Customer customer = customerRepository.findByCustomerId(loanRequest.getCustomerId());
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with customerId: " + loanRequest.getCustomerId());
        }

        Loan loan = Loan.builder()
                .amount(loanRequest.getAmount())
                .customerFullName(loanRequest.getCustomerFullName())
                .customer(customer)
                .loanType(loanRequest.getLoanType())
                .termMonths(loanRequest.getTermMonths())
                .status(String.valueOf(LoanStatus.PENDING))
                .build();

        Loan saved = loanRepository.save(loan);
        return toResponse(saved);
    }

    @Override
    @Cacheable(value = "totalLoanAmount", key = "#customerId")
    public BigDecimal getLoanAmountByCustomerId(Long customerId) {
        return loanRepository.sumAmountByCustomerId(customerId);
    }

    @Override
    public LoanResponse getLoanByLoanId(Long id) throws LoanNotFoundException {
        Optional<Loan> opt = loanRepository.findById(id);
        Loan loan = opt.orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id));
        return toResponse(loan);
    }

    @Override
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<LoanResponse> getLoansByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with customerId: " + customerId + " not found.");
        }
        return loanRepository.findByCustomerCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanResponse updateLoan(Long id, LoanRequest request) throws LoanNotFoundException, CustomerNotFoundException {
        Loan existing = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id));

        Customer customer = customerRepository.findByCustomerId(request.getCustomerId());
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found with customerId: " + request.getCustomerId());
        }

        existing.setAmount(request.getAmount());
        existing.setLoanType(request.getLoanType());
        existing.setTermMonths(request.getTermMonths());

        Loan saved = loanRepository.save(existing);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteLoan(Long id) throws LoanNotFoundException {
        Loan existing = loanRepository.findById(id).orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + id));
        loanRepository.delete(existing);
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .status(loan.getStatus())
                .termMonths(loan.getTermMonths())
                .customerFullName(loan.getCustomerFullName())
                .customerId(loan.getCustomer() != null ? loan.getCustomer().getCustomerId() : null)
                .amount(loan.getAmount())
                .build();
    }

}
