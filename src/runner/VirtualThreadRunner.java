package runner;

import model.Card;
import simulation.MonteCarloSimulator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class VirtualThreadRunner {

    public void run(List<Card> playerCards, int numberOfSimulations, int virtualThreadCount) throws InterruptedException, ExecutionException {
        System.out.println("Запуск симуляции для руки: " + playerCards);
        System.out.println("Количество итераций: " + numberOfSimulations);
        System.out.println("Режим: Виртуальные потоки (" + virtualThreadCount + ")");

        int chunkSize = numberOfSimulations / virtualThreadCount;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        Set<String> threadNames = ConcurrentHashMap.newKeySet();

        Callable<Integer> task = () -> {
            threadNames.add(Thread.currentThread().toString());
            MonteCarloSimulator simulator = new MonteCarloSimulator();
            int wins = 0;
            for (int i = 0; i < chunkSize; i++) {
                if (simulator.runSingleSimulation(playerCards)) {
                    wins++;
                }
            }
            return wins;
        };

        long startTime = System.nanoTime();

        List<Future<Integer>> futures = executor.invokeAll(
                java.util.Collections.nCopies(virtualThreadCount, task)
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
        System.out.println("Уникальных потоков: " + threadNames.size());

        RunnerStats.printAll();
    }
}
