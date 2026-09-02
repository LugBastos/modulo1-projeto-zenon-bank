package br.com.zenon.zenonfrauddetector.repository;

import br.com.zenon.zenonfrauddetector.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByOriginName(String originName);
}
