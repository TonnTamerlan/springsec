package ua.oleksii.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.bank.model.Customer;
import ua.oleksii.bank.repository.CustomerRepository;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        try {
            String hashPsw = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPsw);
            customerRepository.save(customer);
            if (customer.getId() > 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Customer created");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer is not created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception ocured" + e.getMessage());
        }
    }
}
