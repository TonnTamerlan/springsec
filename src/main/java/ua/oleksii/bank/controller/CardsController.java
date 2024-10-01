package ua.oleksii.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.bank.model.Cards;
import ua.oleksii.bank.model.Customer;
import ua.oleksii.bank.repository.CardsRepository;
import ua.oleksii.bank.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private final CardsRepository cardsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            List<Cards> cards = cardsRepository.findByCustomerId(customer.get().getId());
            if (cards != null) {
                return cards;
            }
        }
        return null;
    }

}
