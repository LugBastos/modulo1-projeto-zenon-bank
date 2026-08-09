package br.com.zenon.zenonfrauddetector.dto;

import java.math.BigDecimal;

public record Client(
        String nameDest,
        BigDecimal oldBalanceDest,
        BigDecimal newBalanceDest
) {
}
