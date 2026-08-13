package br.com.zenon.zenonfrauddetector.domain;

import java.math.BigDecimal;

public record TransactionParty(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
}
