package br.com.zenon.zenonfrauddetector.ingestion;

import java.io.IOException;
import java.io.UncheckedIOException;
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

    public TransactionReportResult generate(String fileName) {
        Path file = DATA_DIR.resolve(fileName);

        try (Stream<String> lines = Files.lines(file)) {
            return lines
                    .skip(1) // ignora o cabecalho
                    .filter(line -> !line.isBlank())
                    .map(TransactionCsvRow::parse)
                    .collect(TransactionTotals::new, TransactionTotals::add, TransactionTotals::merge)
                    .toResult();
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler " + file, e);
        }
    }
}
