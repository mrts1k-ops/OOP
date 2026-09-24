package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;


class DeckTest {


    // Берёт из колоды нужное количество карт
    private List<Card> drawCards(Deck deck, int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cards.add(deck.draw());
        }
        return cards;
    }


    // Считает, сколько раз в списке встречается достоинство
    private int countRank(List<Card> cards, Rank rank) {
        int count = 0;
        for (Card card : cards) {
            if (card.getRank() == rank) {
                count++;
            }
        }
        return count;
    }


    // Считает, сколько разных карт в списке
    private int countDifferent(List<Card> cards) {
        Set<String> names = new HashSet<>();
        for (Card card : cards) {
            names.add(card.getName());
        }
        return names.size();
    }


    @Test
    void drawnCardIsNotNull() {
        Deck deck = new Deck(1);
        assertNotNull(deck.draw());
    }


    @Test
    void oneDeckHasFiftyTwoDifferentCards() {
        Deck deck = new Deck(1);
        List<Card> cards = drawCards(deck, 52);
        assertEquals(52, countDifferent(cards));
    }


    @Test
    void oneDeckHasEachRankFourTimes() {
        Deck deck = new Deck(1);
        List<Card> cards = drawCards(deck, 52);
        for (Rank rank : Rank.values()) {
            assertEquals(4, countRank(cards, rank));
        }
    }


    @Test
    void twoDecksHaveEachRankEightTimes() {
        Deck deck = new Deck(2);
        List<Card> cards = drawCards(deck, 104);
        assertEquals(52, countDifferent(cards));
        for (Rank rank : Rank.values()) {
            assertEquals(8, countRank(cards, rank));
        }
    }


    @Test
    void threeDecksHaveEachRankTwelveTimes() {
        Deck deck = new Deck(3);
        List<Card> cards = drawCards(deck, 156);
        for (Rank rank : Rank.values()) {
            assertEquals(12, countRank(cards, rank));
        }
    }


    @Test
    void deckRefillsWhenEmpty() {
        Deck deck = new Deck(1);
        drawCards(deck, 52); // Забираем все карты
        assertNotNull(deck.draw()); // Пятьдесят третья карта не должна вызвать ошибку
    }


    @Test
    void secondPassAfterRefillHasAllCards() {
        Deck deck = new Deck(1);
        drawCards(deck, 52);
        List<Card> secondPass = drawCards(deck, 52);
        assertEquals(52, countDifferent(secondPass));
    }


    @Test
    void decksAreShuffled() {
        List<Card> first = drawCards(new Deck(1), 52);
        List<Card> second = drawCards(new Deck(1), 52);

        // Две перемешанные колоды почти наверняка идут в разном порядке
        boolean sameOrder = true;
        for (int i = 0; i < 52; i++) {
            if (!first.get(i).getName().equals(second.get(i).getName())) {
                sameOrder = false;
            }
        }
        assertFalse(sameOrder);
    }
}