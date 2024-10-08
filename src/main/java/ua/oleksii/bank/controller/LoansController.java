package ua.oleksii.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.bank.model.Customer;
import ua.oleksii.bank.model.Loans;
import ua.oleksii.bank.repository.CustomerRepository;
import ua.oleksii.bank.repository.LoanRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;


    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customer.get().getId());
            if (loans != null) {
                return loans;
            }
        }
        return null;
    }

}
