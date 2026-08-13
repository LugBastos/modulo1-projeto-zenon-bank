package br.com.zenon.zenonfrauddetector.ingestion;

import br.com.zenon.zenonfrauddetector.domain.Transaction;

public sealed interface ParseResult permits ParseResult.Success, ParseResult.Failure {
    record Success(Transaction transaction) implements ParseResult {}
    record Failure(String line, String error) implements ParseResult {}
}
