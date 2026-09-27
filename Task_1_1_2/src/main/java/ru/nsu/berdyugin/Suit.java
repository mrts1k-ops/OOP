package ru.nsu.berdyugin;


/**
 * Масть карты.
 */

public enum Suit {
    SPADES("Пики", "Пиковый", "Пиковая"),
    HEARTS("Червы", "Червовый", "Червовая"),
    DIAMONDS("Бубны", "Бубновый", "Бубновая"),
    CLUBS("Трефы", "Трефовый", "Трефовая");

    private final String name; //Название масти.
    private final String male; //Прилагательно мужского рода (Червовый Король).
    private final String female; //Прилагательное женского рода (Пиковая Дама).


    /**
     * Создаёт масть.
     *
     * @param name название масти.
     * @param male прилагательное мужского рода.
     * @param female прилагательное женского.
     */

    Suit(String name, String male, String female) {
        this.name = name;
        this.male = male;
        this.female = female;
    }


    /**
     * Возвращает название масти.
     *
     * @return название масти.
     */

    public String getName() {
        return name;
    }


    /**
     * Возвращает прилагательное мужского рода.
     *
     * @return прилагательное мужского рода.
     */

    public  String getMale() {
        return male;
    }


    /**
     * Возвращает прилагательное женского рода.
     *
     * @return прилагательное женского рода.
     */

    public String getFemale() {
        return female;
    }
}
