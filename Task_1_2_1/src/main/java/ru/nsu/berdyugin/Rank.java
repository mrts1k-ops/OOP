package ru.nsu.berdyugin;


/**
 * Достоинство карты и количество очков за неё.
 */

public enum Rank {
    TWO("Двойка", 2, false),
    THREE("Тройка", 3, false),
    FOUR("Четверка", 4, false),
    FIVE("Пятерка", 5, false),
    SIX("Шестерка", 6, false),
    SEVEN("Семерка", 7, false),
    EIGHT("Восьмерка", 8, false),
    NINE("Девятка", 9, false),
    TEN("Десятка", 10, false),
    JACK("Валет", 10, true),
    QUEEN("Дама", 10, true),
    KING("Король", 10, true),
    ACE("Туз", 11, false);

    private final String name; //Название карты
    private final int value; //Очки
    private final boolean face; //true, если карта с картинкой.


    /**
     * Создаёт достоинство карты.
     *
     * @param name название карты.
     * @param value очков за карту.
     * @param face true, если это карта с картинкой.
     */

    Rank(String name, int value, boolean face) {
        this.name = name;
        this.value = value;
        this.face = face;
    }


    /**
     * Возвращет название карты.
     *
     * @return название карты.
     */

    public String getName() {
        return name;
    }


    /**
     * Возвращет очки за  карту.
     *
     * @return очки за  карту.
     */

    public int getValue() {
        return value;
    }


    /**
     * Проверяет, есть ли картинка.
     *
     * @return true, если картинка есть.
     */

    public boolean isFace() {
        return face;
    }
}
