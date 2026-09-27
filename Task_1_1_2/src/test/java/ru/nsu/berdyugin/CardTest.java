package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;


class CardTest {


    // Названия карт


    @Test
    void numberCardName() {
        Card card = new Card(Suit.SPADES, Rank.SEVEN);
        assertEquals("Семерка Пики", card.getName());
    }


    @Test
    void numberCardNameHearts() {
        Card card = new Card(Suit.HEARTS, Rank.THREE);
        assertEquals("Тройка Червы", card.getName());
    }


    @Test
    void aceName() {
        Card card = new Card(Suit.CLUBS, Rank.ACE);
        assertEquals("Туз Трефы", card.getName());
    }


    @Test
    void queenNameIsFeminine() {
        Card card = new Card(Suit.SPADES, Rank.QUEEN);
        assertEquals("Пиковая дама", card.getName());
    }


    @Test
    void kingNameIsMasculine() {
        Card card = new Card(Suit.DIAMONDS, Rank.KING);
        assertEquals("Бубновый король", card.getName());
    }


    @Test
    void jackNameIsMasculine() {
        Card card = new Card(Suit.HEARTS, Rank.JACK);
        assertEquals("Червовый валет", card.getName());
    }


    @Test
    void allNamesAreDifferent() {
        Set<String> names = new HashSet<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                String name = new Card(suit, rank).getName();
                assertFalse(name.isEmpty());
                names.add(name);
            }
        }
        assertEquals(52, names.size());
    }


    // Очки


    @Test
    void numberCardValue() {
        assertEquals(2, new Card(Suit.SPADES, Rank.TWO).getValue());
        assertEquals(9, new Card(Suit.SPADES, Rank.NINE).getValue());
        assertEquals(10, new Card(Suit.SPADES, Rank.TEN).getValue());
    }


    @Test
    void faceCardsAreWorthTen() {
        assertEquals(10, new Card(Suit.HEARTS, Rank.JACK).getValue());
        assertEquals(10, new Card(Suit.HEARTS, Rank.QUEEN).getValue());
        assertEquals(10, new Card(Suit.HEARTS, Rank.KING).getValue());
    }


    @Test
    void aceIsWorthEleven() {
        assertEquals(11, new Card(Suit.CLUBS, Rank.ACE).getValue());
    }


    // Текст карты


    @Test
    void toStringNumberCard() {
        assertEquals("Семерка Пики (7)", new Card(Suit.SPADES, Rank.SEVEN).toString());
    }


    @Test
    void toStringFaceCard() {
        assertEquals("Пиковая дама (10)", new Card(Suit.SPADES, Rank.QUEEN).toString());
    }


    @Test
    void toStringAce() {
        assertEquals("Туз Трефы (11)", new Card(Suit.CLUBS, Rank.ACE).toString());
    }


    @Test
    void getRank() {
        assertEquals(Rank.KING, new Card(Suit.HEARTS, Rank.KING).getRank());
    }
}