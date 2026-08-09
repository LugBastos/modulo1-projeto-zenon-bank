package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.dto.Client;
import br.com.zenon.zenonfrauddetector.dto.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class ZenonFraudDetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZenonFraudDetectorApplication.class, args);

        Transaction transaction1 = new Transaction(
                1,
                Type.PAYMENT,
                new BigDecimal("9839.64"),
                new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new Client("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                0,
                0
        );

        Transaction transaction2 = new Transaction(
                743,
                Type.CASH_OUT,
                new BigDecimal("850002.52"),
                new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new Client("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                1,
                0
        );

        System.out.println(transaction1);
        System.out.println(transaction2);

    }

}
