package model;

import java.util.Objects;

public record Card(Rank rank, Suit suit) implements Comparable<Card> {

    @Override
    public int compareTo(Card other) {
        return this.rank.ordinal() - other.rank.ordinal();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}