package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class HandTest {

    //Создаёт карту пик с нужным достоинством
    private Card card(Rank rank) {
        return new Card(Suit.SPADES, rank);
    }

    //Собирает руку из карт
    private Hand handOf(Card... cards) {
        Hand hand = new Hand();
        for (Card card : cards) {
            hand.add(card);
        }
        return hand;
    }

    //Подсчёт очков

    @Test
    void emptyHand() {
        Hand hand = new Hand();
        assertEquals(0, hand.getScore());
    }


    @Test
    void numberCards() {
        Hand hand = handOf(card(Rank.SEVEN), card(Rank.EIGHT));
        assertEquals(15, hand.getScore());
    }


    @Test
    void faceCardsAreWorthTen() {
        Hand hand = handOf(card(Rank.KING), card(Rank.QUEEN));
        assertEquals(20, hand.getScore());
    }


    @Test
    void aceIsEleven() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.SIX));
        assertEquals(17, hand.getScore());
    }


    @Test
    void aceBecomesOneOnBust() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.FIVE), card(Rank.KING));
        assertEquals(16, hand.getScore());
    }


    @Test
    void aceBecomesOneAfterNewCard() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.NINE));
        assertEquals(20, hand.getScore());
        hand.add(card(Rank.FIVE)); // Туз должен стать единицей
        assertEquals(15, hand.getScore());
    }


    @Test
    void twoAces() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.ACE));
        assertEquals(12, hand.getScore());
    }


    @Test
    void twoAcesAndKing() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.ACE), card(Rank.KING));
        assertEquals(12, hand.getScore());
    }


    @Test
    void threeAces() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.ACE), card(Rank.ACE));
        assertEquals(13, hand.getScore());
    }


    @Test
    void fourAces() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.ACE), card(Rank.ACE), card(Rank.ACE));
        assertEquals(14, hand.getScore());
    }


    @Test
    void exactlyTwentyOne() {
        Hand hand = handOf(card(Rank.KING), card(Rank.SEVEN), card(Rank.FOUR));
        assertEquals(21, hand.getScore());
    }


    // Перебор


    @Test
    void bust() {
        Hand hand = handOf(card(Rank.KING), card(Rank.QUEEN), card(Rank.FIVE));
        assertEquals(25, hand.getScore());
        assertTrue(hand.isBust());
    }


    @Test
    void twentyOneIsNotBust() {
        Hand hand = handOf(card(Rank.KING), card(Rank.SEVEN), card(Rank.FOUR));
        assertFalse(hand.isBust());
    }


    @Test
    void aceHelpsToAvoidBust() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.KING), card(Rank.QUEEN));
        assertEquals(21, hand.getScore());
        assertFalse(hand.isBust());
    }


    // Блэкджек


    @Test
    void blackjackAceAndKing() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.KING));
        assertTrue(hand.isBlackJack());
    }


    @Test
    void blackjackAceAndTen() {
        Hand hand = handOf(card(Rank.TEN), card(Rank.ACE));
        assertTrue(hand.isBlackJack());
    }


    @Test
    void twentyOneFromThreeCardsIsNotBlackjack() {
        Hand hand = handOf(card(Rank.SEVEN), card(Rank.SEVEN), card(Rank.SEVEN));
        assertEquals(21, hand.getScore());
        assertFalse(hand.isBlackJack());
    }


    @Test
    void twentyFromTwoCardsIsNotBlackjack() {
        Hand hand = handOf(card(Rank.ACE), card(Rank.NINE));
        assertFalse(hand.isBlackJack());
    }


    @Test
    void twoTensAreNotBlackjack() {
        Hand hand = handOf(card(Rank.TEN), card(Rank.KING));
        assertFalse(hand.isBlackJack());
    }


    @Test
    void emptyHandIsNotBlackjack() {
        assertFalse(new Hand().isBlackJack());
    }


    // Работа с картами в руке


    @Test
    void clearRemovesAllCards() {
        Hand hand = handOf(card(Rank.KING), card(Rank.QUEEN));
        hand.clear();
        assertEquals(0, hand.getScore());
    }


    @Test
    void getCardReturnsCardsInOrder() {
        Card first = card(Rank.TWO);
        Card second = card(Rank.NINE);
        Hand hand = handOf(first, second);
        assertEquals(first, hand.getCard(0));
        assertEquals(second, hand.getCard(1));
    }


    // Вывод в консоль (как в примере из задания)


    @Test
    void describeShowsAllCardsAndScore() {
        Hand hand = handOf(new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.THREE));
        assertEquals("[Пиковая дама (10), Тройка Червы (3)] ⇒ 13", hand.describe());
    }


    @Test
    void describeShowsAceAsEleven() {
        Hand hand = handOf(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.THREE));
        assertEquals("[Туз Трефы (11), Тройка Трефы (3)] ⇒ 14", hand.describe());
    }


    @Test
    void describeShowsAceAsOneAfterBust() {
        Hand hand = handOf(
                new Card(Suit.CLUBS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.THREE),
                new Card(Suit.SPADES, Rank.TEN));
        assertEquals(
                "[Туз Трефы (1), Тройка Трефы (3), Десятка Пики (10)] ⇒ 14", hand.describe());
    }


    @Test
    void describeWithTwoAcesShowsOneAsOne() {
        Hand hand = handOf(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
        assertEquals("[Туз Трефы (1), Туз Червы (11)] ⇒ 12", hand.describe());
    }


    @Test
    void describeHiddenShowsOnlyFirstCard() {
        Hand hand = handOf(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.SPADES, Rank.KING));
        assertEquals("[Туз Трефы (11), <закрытая карта>]", hand.describeHidden());
    }
}