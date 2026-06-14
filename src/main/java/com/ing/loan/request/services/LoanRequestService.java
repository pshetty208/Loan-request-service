package com.ing.loan.request.services;

import com.ing.loan.request.dto.LoanRequest;
import com.ing.loan.request.dto.LoanResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.exception.LoanNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface LoanRequestService {

    LoanResponse createLoan(LoanRequest loanRequest) throws CustomerNotFoundException;

    BigDecimal getLoanAmountByCustomerId(Long customerId);

    LoanResponse getLoanByLoanId(Long id) throws LoanNotFoundException;

    List<LoanResponse> getAllLoans();

    List<LoanResponse> getLoansByCustomerId(Long customerId) throws CustomerNotFoundException;

    LoanResponse updateLoan(Long id, LoanRequest request) throws LoanNotFoundException, CustomerNotFoundException;

    void deleteLoan(Long id) throws LoanNotFoundException;

}
