package ua.oleksii.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

	@GetMapping("/myBalance")
	public String getBalanceDetails() {
		return "get balance from DB";
	}
}
