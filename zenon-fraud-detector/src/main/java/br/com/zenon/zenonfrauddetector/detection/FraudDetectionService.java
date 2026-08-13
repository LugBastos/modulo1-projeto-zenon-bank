package br.com.zenon.zenonfrauddetector.detection;

import br.com.zenon.zenonfrauddetector.domain.Transaction;
import br.com.zenon.zenonfrauddetector.domain.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FraudDetectionService {
    List<Transaction> findFrauds(String file);

    int countFrauds(List<Transaction> frauds);

    List<Transaction> findTopFraudsByAmount(List<Transaction> frauds, int limit);

    List<String> findTopSuspiciousClients(List<Transaction> frauds, int limit);

    BigDecimal calculateTotalLoss(List<Transaction> frauds);

    Map<TransactionType, Long> countFraudsByTransactionType(List<Transaction> frauds);
}
