package ru.nsu.berdyugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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


    // Находит последнее число в тексте по шаблону (или -1, если не нашлось)
    private int lastNumber(String text, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int result = -1;
        while (matcher.find()) {
            result = Integer.parseInt(matcher.group(1));
        }
        return result;
    }


    // Проверяет, что один раунд сыгран по правилам
    private void checkRound(String round) {
        boolean won = round.contains("Вы выиграли раунд!");
        boolean lost = round.contains("Вы проиграли раунд!");
        boolean draw = round.contains("Ничья!");


        // В раунде ровно один результат
        assertEquals(1, count(round, "Вы выиграли раунд!") + count(round, "Вы проиграли раунд!")
                + count(round, "Ничья!"));

        boolean playerBlackjack = round.contains("У вас блэкджек!");
        boolean dealerBlackjack = round.contains("У дилера блэкджек!");
        boolean bothBlackjack = round.contains("У обоих блэкджек!");
        boolean dealerTurn = round.contains("Ход дилера");
        int playerScore = lastNumber(round, "Ваши карты: .*⇒ (\\d+)");
        int dealerScore = lastNumber(round, "Карты дилера: .*⇒ (\\d+)");

        if (bothBlackjack) {
            assertTrue(draw); // У обоих блэкджек - ничья
        } else if (playerBlackjack) {
            assertTrue(won); // Блэкджек делает победителем
        } else if (dealerBlackjack) {
            assertTrue(lost);
        } else if (playerScore > 21) {
            assertTrue(lost); // Перебор игрока - сразу проигрыш
            assertFalse(dealerTurn); // Дилер после этого не ходит
        } else {
            assertTrue(dealerTurn);
            assertTrue(dealerScore >= 17); // Дилер берёт карты минимум до 17
            if (dealerScore > 21) {
                assertTrue(won); // Перебор дилера - победа игрока
            } else if (playerScore > dealerScore) {
                assertTrue(won);
            } else if (playerScore < dealerScore) {
                assertTrue(lost);
            } else {
                assertTrue(draw);
            }
        }
    }

    // Проверяет всю игру: каждый раунд и итоговый счёт
    private void checkGame(String output) {
        String[] rounds = output.split("Раунд \\d+");
        assertTrue(rounds.length >= 2); // Хотя бы один раунд сыгран

        for (int i = 1; i < rounds.length; i++) {
            checkRound(rounds[i]);
        }


        // Последний напечатанный счёт должен совпадать с реальным числом побед
        int wins = count(output, "Вы выиграли раунд!");
        int losses = count(output, "Вы проиграли раунд!");
        Matcher matcher = Pattern.compile("Счет (\\d+):(\\d+)").matcher(output);
        int shownWins = -1;
        int shownLosses = -1;
        while (matcher.find()) {
            shownWins = Integer.parseInt(matcher.group(1));
            shownLosses = Integer.parseInt(matcher.group(2));
        }
        assertEquals(wins, shownWins);
        assertEquals(losses, shownLosses);
    }


    // Один раунд: игрок сразу останавливается, потом выходит
    private String playOneRound(int decks) {
        return run("0\n0\n", () -> new Game(decks).start());
    }


    @Test
    void gameStarts() {
        String output = playOneRound(1);
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Раунд 1"));
        assertTrue(output.contains("Дилер раздал карты"));
    }


    @Test
    void dealerCardIsHiddenAtStart() {
        String output = playOneRound(1);
        assertTrue(output.contains("<закрытая карта>"));
    }


    @Test
    void firstScoreIsCorrect() {
        String output = playOneRound(1);
        boolean correct = output.contains("Счет 1:0 в вашу пользу.")
                || output.contains("Счет 0:1 в пользу дилера.")
                || output.contains("Счет 0:0.");
        assertTrue(correct);
    }


    @Test
    void manySingleRoundsFollowRules() {
        for (int i = 0; i < 300; i++) {
            checkGame(playOneRound(1));
        }
    }


    @Test
    void severalDecksFollowRules() {
        for (int i = 0; i < 100; i++) {
            checkGame(playOneRound(4));
        }
    }


    @Test
    void manyRoundsInRow() {
        // Пары "стоп" и "играть ещё"; в конце "стоп" и "выйти"
        String input = "0\n1\n".repeat(30) + "0\n0\n";
        for (int i = 0; i < 30; i++) {
            String output = run(input, () -> new Game(1).start());
            checkGame(output);
        }
    }


    @Test
    void playerHitsUntilBust() {
        // Игрок много раз подряд берёт карту, значит, часто будет перебор
        String input = "1\n".repeat(15) + "0\n0\n";
        for (int i = 0; i < 100; i++) {
            String output = run(input, () -> new Game(1).start());
            checkGame(output);
        }
    }
}