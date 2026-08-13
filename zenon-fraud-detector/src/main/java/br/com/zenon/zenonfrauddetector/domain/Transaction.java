package br.com.zenon.zenonfrauddetector.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        TransactionType transactionType,
        BigDecimal amount,
        TransactionParty origin,
        TransactionParty recipient,
        boolean isFraud,
        boolean isFlaggedFraud) {

    public Transaction {
        Objects.requireNonNull(transactionType, "Type cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(origin, "Customer cannot be null");
        Objects.requireNonNull(recipient, "Client cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if (step < 1) {
            throw new IllegalArgumentException("Step cannot be less than 1");
        }
    }

    public static Transaction fromCsv(String line) {
        String[] col = line.split(",");
        return new Transaction(
                Integer.parseInt(col[0]),
                TransactionType.valueOf(col[1]),
                new BigDecimal(col[2]),
                new TransactionParty(col[3], new BigDecimal(col[4]), new BigDecimal(col[5])),
                new TransactionParty(col[6], new BigDecimal(col[7]), new BigDecimal(col[8])),
                Integer.parseInt(col[9]) == 1,
                Integer.parseInt(col[10]) == 1
        );
    }
}
