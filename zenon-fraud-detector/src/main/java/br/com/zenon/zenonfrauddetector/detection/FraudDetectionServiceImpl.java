package br.com.zenon.zenonfrauddetector.detection;

import br.com.zenon.zenonfrauddetector.domain.Transaction;
import br.com.zenon.zenonfrauddetector.domain.TransactionType;
import br.com.zenon.zenonfrauddetector.ingestion.ParseResult;
import br.com.zenon.zenonfrauddetector.ingestion.TransactionIngestor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final TransactionIngestor transactionIngestor;

    public FraudDetectionServiceImpl (TransactionIngestor transactionIngestor) {
        this.transactionIngestor = transactionIngestor;
    }

    @Override
    public List<Transaction> findFrauds(String file) {
        List<ParseResult> results = transactionIngestor.transactionList(file);

        return results.stream()
                .filter(result -> result instanceof ParseResult.Success)
                .map(result -> (ParseResult.Success) result)
                .map(ParseResult.Success::transaction)
                .filter(Transaction::isFraud)
                .toList()
                ;
    }

    @Override
    public int countFrauds(List<Transaction> frauds) {
        return frauds.size();
    }

    @Override
    public List<Transaction> findTopFraudsByAmount(List<Transaction> frauds, int limit) {
        return frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(limit)
                .toList();
    }

    @Override
    public List<String> findTopSuspiciousClients(List<Transaction> frauds, int limit) {
        return frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(limit)
                .toList();
    }

    @Override
    public BigDecimal calculateTotalLoss(List<Transaction> frauds) {
        return frauds.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<TransactionType, Long> countFraudsByTransactionType(List<Transaction> frauds) {
        return frauds.stream()
                .collect(Collectors.groupingBy(
                        Transaction::transactionType,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }
}
