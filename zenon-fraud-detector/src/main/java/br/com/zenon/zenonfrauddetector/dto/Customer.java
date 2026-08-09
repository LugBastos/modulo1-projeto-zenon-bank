package br.com.zenon.zenonfrauddetector.dto;

import java.math.BigDecimal;

public record Customer(
        String nameOrig,
        BigDecimal oldBalanceOrg,
        BigDecimal newBalanceOrg
) {
}
