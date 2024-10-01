package ua.oleksii.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.bank.model.AccountTransactions;
import ua.oleksii.bank.model.Customer;
import ua.oleksii.bank.repository.AccountTransactionsRepository;
import ua.oleksii.bank.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final AccountTransactionsRepository accountTransactionsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            List<AccountTransactions> accountTransactions = accountTransactionsRepository.
                    findByCustomerIdOrderByTransactionDtDesc(customer.get().getId());
            if (accountTransactions != null) {
                return accountTransactions;
            }
        }
        return null;
    }
}
