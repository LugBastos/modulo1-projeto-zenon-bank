package br.com.zenon.zenonfrauddetector.ingestion;

import java.math.BigDecimal;

/** Agregados calculados pelo {@link TransactionReport} sobre todo o arquivo. */
public record TransactionReportResult(long totalLines, long totalFrauds, BigDecimal totalAmount) {
}
