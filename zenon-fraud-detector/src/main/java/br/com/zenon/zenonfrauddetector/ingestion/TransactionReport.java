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
 * Percorre o arquivo de transacoes de forma "preguicosa" (lazy) com
 * {@link Files#lines(Path)}, processando linha a linha sem carregar o arquivo
 * inteiro na memoria. Em vez de guardar as transacoes, acumula apenas os
 * agregados: total de linhas, total de fraudes e valor total transacionado.
 */
public class TransactionReport {

    private static final int COLUMN_AMOUNT = 2;
    private static final int COLUMN_IS_FRAUD = 9;

    public record Result(long totalLines, long totalFrauds, BigDecimal totalAmount) {
    }

    public Result generate(String file) {
        Path path = Paths.get("data", file);

        try (Stream<String> lines = Files.lines(path)) {
            Accumulator accumulator = lines
                    .skip(1) // cabecalho
                    .filter(line -> !line.isBlank())
                    .collect(Accumulator::new, Accumulator::accept, Accumulator::merge);

            return accumulator.toResult();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler " + path, e);
        }
    }

    private static final class Accumulator {

        private long lines;
        private long frauds;
        private BigDecimal amount = BigDecimal.ZERO;

        private void accept(String line) {
            String[] columns = line.split(",");

            lines++;
            amount = amount.add(new BigDecimal(columns[COLUMN_AMOUNT]));

            if ("1".equals(columns[COLUMN_IS_FRAUD])) {
                frauds++;
            }
        }

        private void merge(Accumulator other) {
            lines += other.lines;
            frauds += other.frauds;
            amount = amount.add(other.amount);
        }

        private Result toResult() {
            return new Result(lines, frauds, amount.setScale(2, RoundingMode.HALF_UP));
        }
    }
}
