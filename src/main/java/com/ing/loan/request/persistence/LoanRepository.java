package com.ing.loan.request.persistence;
import com.ing.loan.request.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT COALESCE(SUM(l.amount), 0) FROM loan l WHERE l.customer.customerId = :customerId")
    BigDecimal sumAmountByCustomerId(@Param("customerId") Long customerId);

    List<Loan> findByCustomerCustomerId(Long customerId);

}
