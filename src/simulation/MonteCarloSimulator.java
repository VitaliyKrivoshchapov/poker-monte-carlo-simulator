package simulation;

import logic.Deck;
import logic.HandEvaluator;
import logic.HandResult;
import model.Card;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloSimulator {

    private final HandEvaluator evaluator = new HandEvaluator();

    /**
     * Запускает одну симуляцию и возвращает true, если игрок выиграл.
     * @param playerCards карты игрока
     * @return true при победе, false при поражении или ничьей
     */
    public boolean runSingleSimulation(List<Card> playerCards) {
        Deck deck = new Deck();
        deck.removeCards(playerCards);

        List<Card> opponentCards = List.of(deck.deal(), deck.deal());
        List<Card> communityCards = List.of(deck.deal(), deck.deal(), deck.deal(), deck.deal(), deck.deal());

        // Собираем полные 7-карточные руки
        List<Card> player7Cards = new ArrayList<>(playerCards);
        player7Cards.addAll(communityCards);

        List<Card> opponent7Cards = new ArrayList<>(opponentCards);
        opponent7Cards.addAll(communityCards);

        // Оцениваем руки
        HandResult playerResult = evaluator.evaluate(player7Cards);
        HandResult opponentResult = evaluator.evaluate(opponent7Cards);

        // Сравниваем
        return playerResult.compareTo(opponentResult) > 0;
    }
}