package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.dto.Client;
import br.com.zenon.zenonfrauddetector.dto.Customer;

import java.math.BigDecimal;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer customer,
        Client client,
        int isFraud,
        int isFlaggedFraud) {
}
