package ru.nsu.berdyugin;


/**
 * Игральная карта: масть и достоинство.
 */

public class Card {
    private final Suit suit; //Масть
    private final Rank rank; //Достоинство


    /**
     * Создаёт карту.
     *
     * @param suit масть карты.
     * @param rank достоинство карты.
     */

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }


    /**
     * Возвращает достоинство карты.
     *
     * @return достоинство карты.
     */

    public Rank getRank() {
        return rank;
    }


    /**
     * Возвращает очки за карту.
     *
     * @return очки за карту.
     */

    public int getValue() {
        return rank.getValue();
    }


    /**
     * Возвращает название карты: "Пиковая дама" и тд.
     *
     * @return название карты.
     */

    public String getName() {
        if (rank.isFace()) {
            String adjective = suit.getMale(); // Валет и Король - мужского рода.
            if (rank == Rank.QUEEN) {
                adjective = suit.getFemale();  // Дама - женского рода.
            }
            return adjective + " " + rank.getName().toLowerCase();
        }

        //Все остальные карты
        return rank.getName() + " " + suit.getName();
    }


    /**
     * Возвращает карту в виде строки.
     *
     * @return строка с названием и очками.
     */

    @Override
    public String toString() {
        return getName() + " (" + getValue() + ")";
    }
}
