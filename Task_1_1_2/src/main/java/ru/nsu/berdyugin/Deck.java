package ru.nsu.berdyugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Колода: одна или несколько колод по 52 карты.
 */

public class Deck {
    private final int decksCount; //Сколько колод используется в игре.
    private final List<Card> cards = new ArrayList<>(); //Карты, которые остались.


    /**
     * Создаёт колоду.
     *
     * @param decksCount количество колод по 52 карты.
     */

    public Deck(int decksCount) {
        this.decksCount = decksCount;
        refill();
    }

    /**
     * Собирает все карты заново и перемешивает их.
     */

    private void refill() {
        cards.clear();
        for (int i = 0; i < decksCount; i++) { //Колода
            for (Suit suit : Suit.values()) { //Масть
                for (Rank rank : Rank.values()) { //Достоинство (ранг, наминал)
                    cards.add(new Card(suit, rank));
                }
            }
        }
        Collections.shuffle(cards); //Перемешиваем
    }

    /**
     * Берёт верхнюю карту из колоды.
     *
     * @return взятая карта.
     */

    public Card draw() {
        if (cards.isEmpty()) { //Если колода закончилась, составляем её заново.
            refill();
        }
        return cards.remove(cards.size() - 1);
    }
}
