package logic;

import model.Card;
import model.Combination;
import model.Rank;
import model.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {

    public HandResult evaluate(List<Card> sevenCards) {
        List<List<Card>> all5CardHands = new ArrayList<>();
        generateCombinations(sevenCards, 5, 0, new ArrayList<>(), all5CardHands);

        HandResult bestResult = null;
        for (List<Card> hand : all5CardHands) {
            HandResult currentResult = evaluate5Cards(hand);
            if (bestResult == null || currentResult.compareTo(bestResult) > 0) {
                bestResult = currentResult;
            }
        }
        return bestResult;
    }

    // Рекурсивный метод для генерации всех 5-карточных комбинаций из 7
    private void generateCombinations(List<Card> cards, int len, int start, List<Card> current, List<List<Card>> result) {
        if (len == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i <= cards.size() - len; i++) {
            current.add(cards.get(i));
            generateCombinations(cards, len - 1, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    private HandResult evaluate5Cards(List<Card> hand) {
        List<Card> sortedHand = hand.stream().sorted().collect(Collectors.toList());

        boolean isFlush = isFlush(sortedHand);
        boolean isStraight = isStraight(sortedHand);

        if (isFlush && isStraight) {
            if (sortedHand.get(4).rank() == Rank.ACE) {
                return new HandResult(Combination.ROYAL_FLUSH, sortedHand);
            }
            return new HandResult(Combination.STRAIGHT_FLUSH, sortedHand);
        }

        Map<Rank, Long> rankCounts = countRanks(sortedHand);

        if (hasNOfAKind(4, rankCounts)) {
            return new HandResult(Combination.FOUR_OF_A_KIND, reorderForNOfAKind(sortedHand, 4, rankCounts));
        }

        if (hasNOfAKind(3, rankCounts) && hasNOfAKind(2, rankCounts)) {
            return new HandResult(Combination.FULL_HOUSE, reorderForNOfAKind(sortedHand, 3, rankCounts));
        }

        if (isFlush) {
            return new HandResult(Combination.FLUSH, sortedHand);
        }

        if (isStraight) {
            return new HandResult(Combination.STRAIGHT, sortedHand);
        }

        if (hasNOfAKind(3, rankCounts)) {
            return new HandResult(Combination.THREE_OF_A_KIND, reorderForNOfAKind(sortedHand, 3, rankCounts));
        }

        if (countPairs(rankCounts) == 2) {
            return new HandResult(Combination.TWO_PAIR, reorderForPairs(sortedHand, rankCounts));
        }

        if (countPairs(rankCounts) == 1) {
            return new HandResult(Combination.ONE_PAIR, reorderForNOfAKind(sortedHand, 2, rankCounts));
        }

        return new HandResult(Combination.HIGH_CARD, sortedHand);
    }

    private boolean isFlush(List<Card> hand) {
        Suit firstSuit = hand.get(0).suit();
        return hand.stream().allMatch(card -> card.suit() == firstSuit);
    }

    private boolean isStraight(List<Card> hand) {
        // Особый случай для стрита от Туза до 5 (A, 2, 3, 4, 5)
        boolean isLowAceStraight = hand.get(0).rank() == Rank.TWO &&
                hand.get(1).rank() == Rank.THREE &&
                hand.get(2).rank() == Rank.FOUR &&
                hand.get(3).rank() == Rank.FIVE &&
                hand.get(4).rank() == Rank.ACE;
        if(isLowAceStraight) return true;

        for (int i = 0; i < hand.size() - 1; i++) {
            if (hand.get(i).rank().ordinal() + 1 != hand.get(i + 1).rank().ordinal()) {
                return false;
            }
        }
        return true;
    }

    private Map<Rank, Long> countRanks(List<Card> hand) {
        return hand.stream().collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
    }

    private boolean hasNOfAKind(int n, Map<Rank, Long> rankCounts) {
        return rankCounts.containsValue((long) n);
    }

    private int countPairs(Map<Rank, Long> rankCounts) {
        return (int) rankCounts.values().stream().filter(count -> count == 2).count();
    }

    // Вспомогательные методы для правильной сортировки карт для сравнения кикеров
    private List<Card> reorderForNOfAKind(List<Card> hand, int n, Map<Rank, Long> counts) {
        List<Card> reordered = new ArrayList<>();
        Rank ofAKindRank = counts.entrySet().stream().filter(e -> e.getValue() == n).findFirst().get().getKey();
        hand.stream().filter(c -> c.rank() == ofAKindRank).forEach(reordered::add);
        hand.stream().filter(c -> c.rank() != ofAKindRank).forEach(reordered::add);
        return reordered;
    }

    private List<Card> reorderForPairs(List<Card> hand, Map<Rank, Long> counts) {
        List<Card> reordered = new ArrayList<>();
        List<Rank> pairRanks = counts.entrySet().stream()
                .filter(e -> e.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        hand.stream().filter(c -> c.rank() == pairRanks.get(0)).forEach(reordered::add);
        hand.stream().filter(c -> c.rank() == pairRanks.get(1)).forEach(reordered::add);
        hand.stream().filter(c -> !pairRanks.contains(c.rank())).forEach(reordered::add);
        return reordered;
    }
}