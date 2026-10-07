package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Выражение-константа: просто число.
 */

public class Number extends Expression {
    private final int value;


    /**
     * Создаёт константу.
     *
     * @param value значение константы.
     */

    public Number(int value) {
        this.value = value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0); // Производная константы всегда 0
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return value;
    }
}