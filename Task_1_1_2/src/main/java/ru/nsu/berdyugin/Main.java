package ru.nsu.berdyugin;

/**
 * Объявляем начало функции.
 */

public class Main {

    /**
     * Запускаем программу
     *
     * @param args аргументы командой строки.
     */

    public static void main (String[] args) {
        Game game = new Game(1); //Играем одной колодой
        game.start();
    }
}
