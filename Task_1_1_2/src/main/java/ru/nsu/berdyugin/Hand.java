package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.List;


/**
 * Карта на руках у участников и подсчёт очков.
 */

public class Hand {
    private final List<Card> cards = new ArrayList<>();


    /**
     * Добавляет карту в руку.
     *
     * @param card добавляемая карта.
     */

    public void add(Card card) {
        cards.add(card);
    }


    /**
     * Убираем все карты из руки.
     */

    public void clear() {
        cards.clear();
    }


    /**
     * Возвращаем карту по номеру (с нуля).
     *
     * @param index номер карты.
     * @return с этим номаром.
     */

    public Card getCard(int index) {
        return cards.get(index);
    }


    /**
     * Считает сумму очков, где каждый туз стоит 11.
     *
     * @return сумма очков без пересчётов тузов.
     */

    private int sumWithAcesAsEleven() {
        int sum = 0;
        for (Card card : cards) {
            sum += card.getValue();
        }
        return sum;
    }


    /**
     * Считает итоговую сумму очков: туз стоит 11, а если сумма больше 21, то 1.
     *
     * @return сумма очков.
     */

    public int getScore() {
        int score = sumWithAcesAsEleven();

        //Считаем тузы.
        int aces = 0;
        for (Card card : cards) {
            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        //Пока перебор, превращаем тузы из 11 в 1.
        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }


    /**
     * Проверяет, есть ли блэкджек (две карты на 21 очко).
     *
     * @return true, если это блэкджек.
     */

    public boolean isBlackJack() {
        return cards.size() == 2 && getScore() == 21;
    }


    /**
     * Проверяет, есть ли перебор.
     *
     * @return true, если сумма больше 21.
     */

    public boolean isBust() {
        return getScore() > 21;
    }


    /**
     * Показывает все карты и сумму: "[Пиковая дама (10), Тройка Червы (3)] \u21D2 13".
     *
     * @return строка с картами и суммой.
     */

    public String describe() {
        //Сколько тузов сейчас считается за 1.
        int acesAsOne = (sumWithAcesAsEleven() - getScore()) / 10;

        String result = "[";
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            int value = card.getValue();

            //Если туз пересчитан, показываем его как 1.
            if (card.getRank() == Rank.ACE && acesAsOne > 0) {
                value = 1;
                acesAsOne--;
            }

            if (i > 0) {
                result += ", ";
            }
            result += card.getName() + " (" + value + ")";
        }
        result += "] ⇒ " + getScore();
        return result;
    }


    /**
     * Показывает только первую карту, вторая закрыта (для дилера в начале раунда).
     *
     * @return строка с одной открытой картой.
     */

    public String describeHidden() {
        return "[" + cards.get(0) + ", <закрытая карта>]";
    }
}
