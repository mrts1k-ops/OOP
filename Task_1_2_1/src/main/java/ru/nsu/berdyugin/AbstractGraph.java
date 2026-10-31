package ru.nsu.berdyugin;


/**
 * Общая часть для всех реализаций Graph: equals(); hashCode() и toString().
 */

public abstract class AbstractGraph implements Graph {

    @Override
    public boolean equals(Object other) {
        return GraphUtils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return GraphUtils.hashCode(this);
    }

    @Override
    public String toString() {
        return GraphUtils.toString(this);
    }
}