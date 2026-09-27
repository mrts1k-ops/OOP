package ru.nsu.berdyugin;

/**
 * Объявляем начало функции.
 */

public class Main {

    /**
     * Запускаем программу.
     *
     * @param args аргументы командой строки.
     */

    public static void main(String[] args) {
        Deck deck = new Deck(1); // Одна колода
        Game game = new Game(deck); //Играем одной колодой
        game.start();
    }
}
