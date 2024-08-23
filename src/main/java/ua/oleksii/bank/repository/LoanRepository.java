package ua.oleksii.bank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import ua.oleksii.bank.model.Loans;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {

    @PreAuthorize("hasRole('USER')")
    List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}
