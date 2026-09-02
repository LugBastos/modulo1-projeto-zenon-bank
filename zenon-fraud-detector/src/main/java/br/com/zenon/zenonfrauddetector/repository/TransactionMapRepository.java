package br.com.zenon.zenonfrauddetector.repository;

import br.com.zenon.zenonfrauddetector.domain.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionsByOriginName;

    public TransactionMapRepository(List<Transaction> transactions) {
        Map<String, Transaction> index = new HashMap<>(transactions.size());

        for (Transaction transaction : transactions) {
            index.putIfAbsent(transaction.origin().name(), transaction);
        }

        this.transactionsByOriginName = Map.copyOf(index);
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return Optional.ofNullable(transactionsByOriginName.get(originName));
    }

    public int size() {
        return transactionsByOriginName.size();
    }
}
