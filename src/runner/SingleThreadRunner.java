package runner;

import model.Card;
import simulation.MonteCarloSimulator;

import java.util.List;

public class SingleThreadRunner {

    public void run(List<Card> playerCards, int numberOfSimulations) {
        System.out.println("Запуск симуляции для руки: " + playerCards);
        System.out.println("Количество итераций: " + numberOfSimulations);
        System.out.println("Режим: Однопоточный");

        MonteCarloSimulator simulator = new MonteCarloSimulator();
        int wins = 0;

        for (int i = 0; i < numberOfSimulations; i++) {
            if (simulator.runSingleSimulation(playerCards)) {
                wins++;
            }
        }

        double winRate = (double) wins / numberOfSimulations * 100;

        System.out.println("----------------------------------------");
        System.out.printf("Побед: %d из %d%n", wins, numberOfSimulations);
        System.out.printf("Винрейт: %.2f%%%n", winRate);
    }
}
