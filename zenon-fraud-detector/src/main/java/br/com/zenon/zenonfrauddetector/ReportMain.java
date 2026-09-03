package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.ingestion.TransactionReport;

/**
 * Executa o {@link TransactionReport} sobre o arquivo PaySim original (~493MB).
 *
 * Rodar com pouca memoria para comprovar o baixo consumo do streaming lazy:
 *   VM Argument: -Xmx128m
 */
public class ReportMain {

    private static final String PAYSIM_FILE = "PS_20174392719_1491204439457_log.csv";

    public static void main(String[] args) {
        TransactionReport report = new TransactionReport();
        TransactionReport.Result result = report.generate(PAYSIM_FILE);

        System.out.println("Total de linhas: " + result.totalLines());
        System.out.println("Total de fraudes: " + result.totalFrauds());
        System.out.println("Valor total transacionado: " + result.totalAmount().toPlainString());
    }
}
