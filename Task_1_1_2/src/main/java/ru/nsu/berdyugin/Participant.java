package ru.nsu.berdyugin;

/**
 * Участник игры (игрок или дилер): у него есть рука с картами.
 */

public abstract class Participant {
    private final Hand hand = new Hand();  //Карты участника

    /**
     * Возвращает руку участника.
     *
     * @return рука с картами.
     */

    public Hand getHand() {
        return hand;
    }

    /**
     * Решат, хочет ли участник взять ещё одну карту.
     * Разные методы на игрока и дилера.
     *
     * @return true, если участник берёт карту.
     */

    public abstract boolean wantsToTake();
}
