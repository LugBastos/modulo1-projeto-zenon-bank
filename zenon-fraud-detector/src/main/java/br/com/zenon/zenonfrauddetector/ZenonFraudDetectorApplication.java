package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.detection.FraudDetectionService;
import br.com.zenon.zenonfrauddetector.domain.Transaction;
import br.com.zenon.zenonfrauddetector.domain.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ZenonFraudDetectorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ZenonFraudDetectorApplication.class);

    private final FraudDetectionService fraudDetectionService;

    public ZenonFraudDetectorApplication(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ZenonFraudDetectorApplication.class, args);

    }

    @Override
    public void run(String... args) {
        String file = "PS_20174392719_1491204439457_log.csv";

        List<Transaction> frauds = fraudDetectionService.findFrauds(file);

        log.info("1. Total de Fraudes: {}", fraudDetectionService.countFrauds(frauds));

        log.info("2. Top 3 Fraudes de Maior Valor:");
        fraudDetectionService.findTopFraudsByAmount(frauds, 3)
                .stream()
                .map(Transaction::amount)
                .forEach(amount -> log.info("{}", amount.setScale(2, RoundingMode.HALF_UP).toPlainString()));

        log.info("3. Clientes Suspeitos:");
        fraudDetectionService.findTopSuspiciousClients(frauds, 5)
                .forEach(client -> log.info("{}", client));

        log.info("4. Prejuízo Total: {}", fraudDetectionService.calculateTotalLoss(frauds)
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString());

        log.info("5. Fraudes por Tipo:");
        Map<TransactionType, Long> fraudsByTransactionType = fraudDetectionService.countFraudsByTransactionType(frauds);

        fraudsByTransactionType.forEach((type, count) -> log.info(" - {}: {}", type, count));
    }
}
