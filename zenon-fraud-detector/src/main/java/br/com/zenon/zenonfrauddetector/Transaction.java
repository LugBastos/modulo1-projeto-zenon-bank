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

    public static Transaction fromCsv(String line) {
        String[] col = line.split(",");
        return new Transaction(
                Integer.parseInt(col[0]),
                Type.valueOf(col[1]),
                new BigDecimal(col[2]),
                new Customer(col[3], new BigDecimal(col[4]), new BigDecimal(col[5])),
                new Client(col[6], new BigDecimal(col[7]), new BigDecimal(col[8])),
                Integer.parseInt(col[9]),
                Integer.parseInt(col[10])
        );
    }
}
