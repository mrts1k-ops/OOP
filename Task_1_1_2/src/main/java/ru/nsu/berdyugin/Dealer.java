package ru.nsu.berdyugin;

/**
 * Дилер: берёт карты, пока сумма меньше 17.
 */

public class Dealer extends Participant {

    /**
     * Дилер берёт карту, если у него меньше 17 очков.
     *
     * @return true, если дилер берёт карту.
     */

    @Override
    public boolean wantsToTake() {
        return getHand().getScore() < 17;
    }
}
