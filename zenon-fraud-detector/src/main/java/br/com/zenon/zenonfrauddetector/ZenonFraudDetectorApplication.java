package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.dto.Client;
import br.com.zenon.zenonfrauddetector.dto.Customer;
import br.com.zenon.zenonfrauddetector.dto.read.TransactionIngestor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ZenonFraudDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZenonFraudDetectorApplication.class, args);

        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> list = transactionIngestor.transactionList("PS_20174392719_1491204439457_log.csv");

        for (int i = 0; i < 10; i++) {
            System.out.println(i + " - " + list.get(i));
        }
    }

}
