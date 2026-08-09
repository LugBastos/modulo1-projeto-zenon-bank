package br.com.zenon.zenonfrauddetector.dto.read;

import br.com.zenon.zenonfrauddetector.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionIngestor {

    public List<Transaction> transactionList(String file) {
        Path path = Paths.get("data", file);

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .limit(1000)
                    .map(Transaction::fromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }
}
