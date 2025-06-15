import model.Card;
import model.Rank;
import model.Suit;
import runner.MultiThreadRunner;
import runner.SingleThreadRunner;
import runner.VirtualThreadRunner;
import runner.RunnerStats;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args){
        List<Card> playerCards = List.of(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES)
        );

        int simulations = 1_000_000;
        int threads = 8;

        System.out.println("=== SingleThreadRunner ===");
        RunnerStats.measure("SingleThreadRunner", () -> {
            new SingleThreadRunner().run(playerCards, simulations);
        });

        System.out.println("\n=== MultiThreadRunner ===");
        RunnerStats.measure("MultiThreadRunner", () -> {
            try {
                new MultiThreadRunner().run(playerCards, simulations, threads);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("\n=== VirtualThreadRunner ===");
        RunnerStats.measure("VirtualThreadRunner", () -> {
            try {
                new VirtualThreadRunner().run(playerCards, simulations, threads);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        RunnerStats.printAll();
    }
}


