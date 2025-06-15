package logic;

import model.Card;
import model.Combination;
import java.util.Collections;
import java.util.List;

public class HandResult implements Comparable<HandResult> {

    private final Combination combination;
    private final List<Card> bestHand; // Лучшие 5 карт

    public HandResult(Combination combination, List<Card> bestHand) {
        this.combination = combination;
        // Сортируем карты в порядке убывания для упрощения сравнения кикеров
        this.bestHand = bestHand.stream()
                .sorted(Collections.reverseOrder())
                .toList();
    }

    @Override
    public int compareTo(HandResult other) {
        // 1. Сравниваем по силе комбинации
        int combinationCompare = Integer.compare(this.combination.getStrength(), other.combination.getStrength());
        if (combinationCompare != 0) {
            return combinationCompare;
        }

        // 2. Если комбинации равны (например, у обоих пара), сравниваем по кикерам
        for (int i = 0; i < this.bestHand.size(); i++) {
            int cardCompare = this.bestHand.get(i).compareTo(other.bestHand.get(i));
            if (cardCompare != 0) {
                return cardCompare;
            }
        }
        return 0; // Полностью равные руки (ничья)
    }

    @Override
    public String toString() {
        return combination + ": " + bestHand;
    }
}