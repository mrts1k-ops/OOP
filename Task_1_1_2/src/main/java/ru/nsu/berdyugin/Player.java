package ru.nsu.berdyugin;

/**
 * Игрок: решает, брать ли карту, по вводу пользователя.
 */

public class Player extends Participant {
    private final UserInput input;   //Читает ответ из консоли.

    /**
     * Создаёт игрока.
     *
     * @param input объект для чтения ввода.
     */

    public Player(UserInput input) {
        this.input = input;
    }

    /**
     * Спрашивает пользователя, брать ли карту.
     *
     * @return true, если пользователь ввёл 1.
     */

    @Override
    public boolean wantsToTake() {
        return input.askYesNo("\nВведите “1”, чтобы взять карту, "
                + "и “0”, чтобы остановиться...");
    }
}
