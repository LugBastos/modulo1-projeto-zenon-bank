package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.detection.FraudDetectionService;
import br.com.zenon.zenonfrauddetector.domain.Transaction;
import br.com.zenon.zenonfrauddetector.ingestion.TransactionIngestor;
import br.com.zenon.zenonfrauddetector.repository.TransactionListRepository;
import br.com.zenon.zenonfrauddetector.repository.TransactionMapRepository;
import br.com.zenon.zenonfrauddetector.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class ZenonFraudDetectorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ZenonFraudDetectorApplication.class);

    private final FraudDetectionService fraudDetectionService;
    private final TransactionIngestor transactionIngestor;

    public ZenonFraudDetectorApplication(FraudDetectionService fraudDetectionService,
                                         TransactionIngestor transactionIngestor) {
        this.fraudDetectionService = fraudDetectionService;
        this.transactionIngestor = transactionIngestor;
    }

    public static void main(String[] args) {
        SpringApplication.run(ZenonFraudDetectorApplication.class, args);

    }

    @Override
    public void run(String... args) {
        String file = "PS_20174392719_1491204439457_log.csv";

//        List<Transaction> frauds = fraudDetectionService.findFrauds(file);
//
//        log.info("1. Total de Fraudes: {}", fraudDetectionService.countFrauds(frauds));
//
//        log.info("2. Top 3 Fraudes de Maior Valor:");
//        fraudDetectionService.findTopFraudsByAmount(frauds, 3)
//                .stream()
//                .map(Transaction::amount)
//                .forEach(amount -> log.info("{}", amount.setScale(2, RoundingMode.HALF_UP).toPlainString()));
//
//        log.info("3. Clientes Suspeitos:");
//        fraudDetectionService.findTopSuspiciousClients(frauds, 5)
//                .forEach(client -> log.info("{}", client));
//
//        log.info("4. Prejuízo Total: {}", fraudDetectionService.calculateTotalLoss(frauds)
//                .setScale(2, RoundingMode.HALF_UP)
//                .toPlainString());
//
//        log.info("5. Fraudes por Tipo:");
//        Map<TransactionType, Long> fraudsByTransactionType = fraudDetectionService.countFraudsByTransactionType(frauds);
//
//        fraudsByTransactionType.forEach((type, count) -> log.info(" - {}: {}", type, count));

        List<Transaction> transactions = transactionIngestor.transactions(file);
        log.info("Transações carregadas: {}", transactions.size());

        TransactionRepository listRepository = new TransactionListRepository(transactions);

        log.info("6.1 Busca na List:");
        search(listRepository, "C12345");
        search(listRepository, "C1231006815");

        String worstCase = transactions.getLast().origin().name();

        log.info("6.2 Pior caso (última transação da lista): {}", worstCase);
        long listTime = measure(listRepository, worstCase, "List");

        TransactionRepository mapRepository = new TransactionMapRepository(transactions);
        long mapTime = measure(mapRepository, worstCase, "Map");

        log.info("6.3 Map foi {}x mais rápido que a List no pior caso",
                mapTime == 0 ? "∞" : listTime / mapTime);
    }

    private void search(TransactionRepository repository, String client) {
        repository.findByOriginName(client)
                .ifPresentOrElse(
                        transaction -> log.info("{}", transaction),
                        () -> log.info("Transação não encontrada para o cliente {}", client)
                );
    }

    private long measure(TransactionRepository repository, String client, String label) {
        long start = System.nanoTime();
        Optional<Transaction> found = repository.findByOriginName(client);
        long elapsed = System.nanoTime() - start;

        log.info("Busca por {} usando {}: {} ns ({} ms) - encontrada: {}",
                client, label, elapsed, elapsed / 1_000_000.0, found.isPresent());

        return elapsed;
    }
}
