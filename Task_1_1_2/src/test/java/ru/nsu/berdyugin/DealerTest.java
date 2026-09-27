package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class DealerTest {

    // Создаёт дилера с двумя картами
    private Dealer dealerWith(Rank first, Rank second) {
        Dealer dealer = new Dealer();
        dealer.getHand().add(new Card(Suit.SPADES, first));
        dealer.getHand().add(new Card(Suit.HEARTS, second));
        return dealer;
    }


    @Test
    void takesCardWithEmptyHand() {
        assertTrue(new Dealer().wantsToTake());
    }


    @Test
    void takesCardBelowSeventeen() {
        assertTrue(dealerWith(Rank.TEN, Rank.SIX).wantsToTake()); // 16
    }


    @Test
    void takesCardOnTwelve() {
        assertTrue(dealerWith(Rank.TWO, Rank.TEN).wantsToTake()); // 12
    }


    @Test
    void stopsOnSeventeen() {
        assertFalse(dealerWith(Rank.TEN, Rank.SEVEN).wantsToTake()); // 17
    }


    @Test
    void stopsAboveSeventeen() {
        assertFalse(dealerWith(Rank.TEN, Rank.NINE).wantsToTake()); // 19
        assertFalse(dealerWith(Rank.KING, Rank.QUEEN).wantsToTake()); // 20
    }


    @Test
    void stopsOnAceAndSix() {
        assertFalse(dealerWith(Rank.ACE, Rank.SIX).wantsToTake()); // 11 + 6 = 17
    }


    @Test
    void takesCardWhenAceBecomesOne() {
        Dealer dealer = dealerWith(Rank.ACE, Rank.FIVE);
        dealer.getHand().add(new Card(Suit.CLUBS, Rank.KING)); // 1 + 5 + 10 = 16
        assertTrue(dealer.wantsToTake());
    }


    @Test
    void stopsAfterBust() {
        Dealer dealer = dealerWith(Rank.TEN, Rank.SIX);
        dealer.getHand().add(new Card(Suit.CLUBS, Rank.KING)); // 26
        assertFalse(dealer.wantsToTake());
    }


    @Test
    void stopsAfterReachingTwentyOne() {
        Dealer dealer = dealerWith(Rank.TEN, Rank.SIX);
        assertTrue(dealer.wantsToTake());
        dealer.getHand().add(new Card(Suit.CLUBS, Rank.FIVE)); // 21
        assertFalse(dealer.wantsToTake());
    }
}