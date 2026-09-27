package ru.nsu.berdyugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;


/**
 * Колода перемешивается случайно, поэтому конкретные карты заранее неизвестны.
 * Тесты играют много раундов и проверяют, что правила игры выполняются всегда.
 */

class GameTest {

    // Запускает код с заданным вводом и возвращает всё, что программа напечатала
    private String run(String input, Runnable action) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setIn(oldIn); // Возвращаем обычную консоль
            System.setOut(oldOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }


    // Считает, сколько раз часть текста встречается в тексте
    private int count(String text, String part) {
        int count = 0;
        int index = text.indexOf(part);
        while (index != -1) {
            count++;
            index = text.indexOf(part, index + part.length());
        }
        return count;
    }


    // Создаёт колоду, где карты выходят в заданном порядке: первая в списке будет взята первой
    private Deck deckWith(Card... cardsInDrawOrder) {
        List<Card> cards = new ArrayList<>(Arrays.asList(cardsInDrawOrder));
        Collections.reverse(cards); // draw() берёт последнюю карту списка
        return new Deck(cards);
    }


    @Test
    void gameStarts() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.DIAMONDS, Rank.NINE));
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Дилер раздал карты"));
    }

    @Test
    void dealerCardIsHiddenAtStart() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.DIAMONDS, Rank.NINE));
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("<закрытая карта>"));
    }

    @Test
    void playerBlackjackWinsImmediately() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.ACE),
                new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.NINE));
        String output = run("0\n", () -> new Game(deck).start());
        assertTrue(output.contains("У вас блэкджек!"));
        assertTrue(output.contains("Вы выиграли раунд! Счет 1:0 в вашу пользу."));
    }

    @Test
    void dealerBlackjackWinsImmediately() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.KING));
        String output = run("0\n", () -> new Game(deck).start());
        assertTrue(output.contains("У дилера блэкджек!"));
        assertTrue(output.contains("Вы проиграли раунд! Счет 0:1 в пользу дилера."));
    }

    @Test
    void bothBlackjackIsDraw() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.ACE),
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.KING));
        String output = run("0\n", () -> new Game(deck).start());
        assertTrue(output.contains("У обоих блэкджек!"));
        assertTrue(output.contains("Ничья! Счет 0:0."));
    }

    @Test
    void playerBustLosesWithoutDealerTurn() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.SIX),
                new Card(Suit.DIAMONDS, Rank.EIGHT),
                new Card(Suit.CLUBS, Rank.KING)); // добор игрока - перебор
        String output = run("1\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Вы проиграли раунд!"));
        assertFalse(output.contains("Ход дилера"));
    }

    @Test
    void playerAutoStopsAtTwentyOne() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.FIVE),
                new Card(Suit.DIAMONDS, Rank.SIX),
                new Card(Suit.SPADES, Rank.SEVEN), // добор игрока: 9 + 5 + 7 = 21
                new Card(Suit.CLUBS, Rank.TEN)); // добор дилера - перебор
        String output = run("1\n0\n", () -> new Game(deck).start());
        assertEquals(1, count(output, "чтобы взять карту")); // вопрос задан только один раз
        assertTrue(output.contains("Вы выиграли раунд!"));
    }

    @Test
    void dealerBustPlayerWins() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.DIAMONDS, Rank.SIX),
                new Card(Suit.HEARTS, Rank.KING)); // добор дилера - перебор
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Ход дилера"));
        assertTrue(output.contains("Вы выиграли раунд!"));
    }

    @Test
    void playerHigherScoreWins() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.EIGHT),
                new Card(Suit.DIAMONDS, Rank.SEVEN));
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Вы выиграли раунд!"));
    }

    @Test
    void dealerHigherScoreWins() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.SEVEN),
                new Card(Suit.DIAMONDS, Rank.NINE));
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Вы проиграли раунд!"));
    }

    @Test
    void equalScoresIsDraw() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.CLUBS, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.NINE));
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Ничья!"));
    }

    @Test
    void dealerHitsMultipleTimesBelowSeventeen() {
        Deck deck = deckWith(
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.TWO),
                new Card(Suit.CLUBS, Rank.KING),
                new Card(Suit.DIAMONDS, Rank.THREE),
                new Card(Suit.SPADES, Rank.FOUR), // 5 -> 9, ещё меньше 17
                new Card(Suit.HEARTS, Rank.TEN)); // 9 -> 19, останавливается
        String output = run("0\n0\n", () -> new Game(deck).start());
        assertEquals(2, count(output, "Дилер открывает карту"));
        assertTrue(output.contains("Вы выиграли раунд!"));
    }

    @Test
    void scoreAccumulatesAcrossTwoRounds() {
        Deck deck = deckWith(
                // Раунд 1: блэкджек игрока - 1:0
                new Card(Suit.SPADES, Rank.ACE),
                new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.HEARTS, Rank.NINE),

                // Раунд 2: перебор игрока - 1:1
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.SEVEN),
                new Card(Suit.CLUBS, Rank.SIX),
                new Card(Suit.DIAMONDS, Rank.EIGHT),
                new Card(Suit.CLUBS, Rank.KING));
        String output = run("1\n1\n0\n", () -> new Game(deck).start());
        assertTrue(output.contains("Счет 1:1."));
    }
}