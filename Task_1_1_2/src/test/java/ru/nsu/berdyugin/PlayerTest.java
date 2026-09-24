package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;
import org.junit.jupiter.api.Test;


class PlayerTest {

    // Создаёт игрока, который "вводит" заданный текст
    private Player playerTyping(String text) {
        return new Player(new UserInput(new Scanner(text)));
    }

    @Test
    void takesCardOnOne() {
        assertTrue(playerTyping("1\n").wantsToTake());
    }


    @Test
    void stopsOnZero() {
        assertFalse(playerTyping("0\n").wantsToTake());
    }


    @Test
    void asksAgainOnWrongInput() {
        assertTrue(playerTyping("abc\n7\n1\n").wantsToTake());
    }


    @Test
    void handIsEmptyAtStart() {
        assertEquals(0, playerTyping("").getHand().getScore());
    }


    @Test
    void handKeepsCards() {
        Player player = playerTyping("");
        player.getHand().add(new Card(Suit.SPADES, Rank.KING));
        player.getHand().add(new Card(Suit.HEARTS, Rank.FIVE));
        assertEquals(15, player.getHand().getScore());
    }
}