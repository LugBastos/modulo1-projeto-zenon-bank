package br.com.zenon.zenonfrauddetector.repository;

import br.com.zenon.zenonfrauddetector.domain.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = List.copyOf(transactions);
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(originName))
                .findFirst();
    }

    public int size() {
        return transactions.size();
    }
}
