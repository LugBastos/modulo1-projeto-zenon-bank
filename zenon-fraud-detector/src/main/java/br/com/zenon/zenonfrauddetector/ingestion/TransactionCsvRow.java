package br.com.zenon.zenonfrauddetector.ingestion;

import java.math.BigDecimal;

/** Campos de uma linha do CSV que interessam ao {@link TransactionReport}. */
record TransactionCsvRow(BigDecimal amount, boolean fraud) {

    private static final String CSV_SEPARATOR = ",";
    private static final int AMOUNT_COLUMN = 2;
    private static final int IS_FRAUD_COLUMN = 9;
    private static final String FRAUD_FLAG = "1";

    static TransactionCsvRow parse(String line) {
        String[] columns = line.split(CSV_SEPARATOR);
        return new TransactionCsvRow(
                new BigDecimal(columns[AMOUNT_COLUMN]),
                FRAUD_FLAG.equals(columns[IS_FRAUD_COLUMN])
        );
    }
}
