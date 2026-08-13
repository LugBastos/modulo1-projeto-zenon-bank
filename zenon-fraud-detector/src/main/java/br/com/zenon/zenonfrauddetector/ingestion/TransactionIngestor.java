package br.com.zenon.zenonfrauddetector.ingestion;

import br.com.zenon.zenonfrauddetector.domain.Transaction;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TransactionIngestor {

    public List<ParseResult> transactionList(String file) {
        Path path = Paths.get("data", file);

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .limit(50000)
                    .map(line -> {
                        try {
                            return (ParseResult) new ParseResult.Success(Transaction.fromCsv(line));
                        } catch (Exception e) {
                            return new ParseResult.Failure(line, e.toString());
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }
}
