package com.ing.loan.request.controller;
import com.ing.loan.request.dto.LoanRequest;
import com.ing.loan.request.dto.LoanResponse;
import com.ing.loan.request.exception.CustomerNotFoundException;
import com.ing.loan.request.exception.LoanNotFoundException;
import com.ing.loan.request.services.LoanRequestService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-service")
public class LoanController {

    private final LoanRequestService loanService;

    public LoanController(LoanRequestService service) {
        this.loanService = service;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoanRequest(@Valid @RequestBody LoanRequest loanRequest) throws CustomerNotFoundException {
        LoanResponse response = loanService.createLoan(loanRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        List<LoanResponse> list = loanService.getAllLoans();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> getLoanByLoanId(@PathVariable Long id) throws LoanNotFoundException {
        LoanResponse resp = loanService.getLoanByLoanId(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/loans/customers/{customerId}")
    public ResponseEntity<List<LoanResponse>> getLoansByCustomerId(@PathVariable Long customerId) throws CustomerNotFoundException {
        List<LoanResponse> list = loanService.getLoansByCustomerId(customerId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/loans/customers/{customerId}/total")
    public ResponseEntity<BigDecimal> getTotalLoanByCustomerId(@PathVariable Long customerId) {
        BigDecimal total = loanService.getLoanAmountByCustomerId(customerId);
        return ResponseEntity.ok(total);
    }

    @PutMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> updateLoan(@PathVariable Long id, @Valid @RequestBody LoanRequest request) throws LoanNotFoundException, CustomerNotFoundException {
        LoanResponse updated = loanService.updateLoan(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/loans/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) throws LoanNotFoundException {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

}
