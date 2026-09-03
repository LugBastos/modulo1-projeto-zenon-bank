package br.com.zenon.zenonfrauddetector.ingestion;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Acumulador mutavel usado na reducao do stream do {@link TransactionReport}. */
final class TransactionTotals {

    private long lines;
    private long frauds;
    private BigDecimal amount = BigDecimal.ZERO;

    void add(TransactionCsvRow row) {
        lines++;
        amount = amount.add(row.amount());
        if (row.fraud()) {
            frauds++;
        }
    }

    void merge(TransactionTotals other) {
        lines += other.lines;
        frauds += other.frauds;
        amount = amount.add(other.amount);
    }

    TransactionReportResult toResult() {
        return new TransactionReportResult(lines, frauds, amount.setScale(2, RoundingMode.HALF_UP));
    }
}
