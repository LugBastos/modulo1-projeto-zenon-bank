package br.com.zenon.zenonfrauddetector.ingestion;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Le o arquivo de transacoes de forma preguicosa (lazy) com {@link Files#lines(Path)},
 * processando uma linha por vez sem carregar o arquivo na memoria. Em vez de guardar
 * as transacoes, mantem apenas os agregados do relatorio.
 */
public class TransactionReport {

    private static final Path DATA_DIR = Paths.get("data");
    private static final String CSV_SEPARATOR = ",";
    private static final int AMOUNT_COLUMN = 2;
    private static final int IS_FRAUD_COLUMN = 9;
    private static final String FRAUD_FLAG = "1";

    /** Agregados calculados sobre todo o arquivo. */
    public record Result(long totalLines, long totalFrauds, BigDecimal totalAmount) {
    }

    public Result generate(String fileName) {
        Path file = DATA_DIR.resolve(fileName);

        try (Stream<String> lines = Files.lines(file)) {
            return lines
                    .skip(1) // ignora o cabecalho
                    .filter(line -> !line.isBlank())
                    .map(Row::parse)
                    .collect(Totals::new, Totals::add, Totals::merge)
                    .toResult();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler " + file, e);
        }
    }

    /** Campos de uma linha do CSV que interessam ao relatorio. */
    private record Row(BigDecimal amount, boolean fraud) {

        static Row parse(String line) {
            String[] columns = line.split(CSV_SEPARATOR);
            return new Row(
                    new BigDecimal(columns[AMOUNT_COLUMN]),
                    FRAUD_FLAG.equals(columns[IS_FRAUD_COLUMN])
            );
        }
    }

    /** Acumulador mutavel usado na reducao do stream. */
    private static final class Totals {

        private long lines;
        private long frauds;
        private BigDecimal amount = BigDecimal.ZERO;

        void add(Row row) {
            lines++;
            amount = amount.add(row.amount());
            if (row.fraud()) {
                frauds++;
            }
        }

        void merge(Totals other) {
            lines += other.lines;
            frauds += other.frauds;
            amount = amount.add(other.amount);
        }

        Result toResult() {
            return new Result(lines, frauds, amount.setScale(2, RoundingMode.HALF_UP));
        }
    }
}
