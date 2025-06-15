package runner;

import model.Card;
import simulation.MonteCarloSimulator;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadRunner {

    public void run(List<Card> playerCards, int numberOfSimulations, int threadCount) throws InterruptedException, ExecutionException {
        System.out.println("Запуск симуляции для руки: " + playerCards);
        System.out.println("Количество итераций: " + numberOfSimulations);
        System.out.println("Режим: Многопоточный (" + threadCount + " потоков)");

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int chunkSize = numberOfSimulations / threadCount;

        Callable<Integer> task = () -> {
            MonteCarloSimulator simulator = new MonteCarloSimulator();
            int wins = 0;
            for (int i = 0; i < chunkSize; i++) {
                if (simulator.runSingleSimulation(playerCards)) {
                    wins++;
                }
            }
            return wins;
        };

        List<Future<Integer>> futures = executor.invokeAll(
                java.util.Collections.nCopies(threadCount, task)
        );
        executor.shutdown();

        int totalWins = 0;
        for (Future<Integer> future : futures) {
            totalWins += future.get();
        }

        double winRate = (double) totalWins / numberOfSimulations * 100;

        System.out.println("----------------------------------------");
        System.out.printf("Побед: %d из %d%n", totalWins, numberOfSimulations);
        System.out.printf("Винрейт: %.2f%%%n", winRate);

        RunnerStats.printAll();
    }
}
