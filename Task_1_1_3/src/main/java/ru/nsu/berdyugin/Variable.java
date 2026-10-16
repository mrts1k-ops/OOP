package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Выражение-переменная, например "x".
 */

public class Variable extends Expression {
    private final String name;


    /**
     * Создаёт переменную.
     *
     * @param name имя переменной (может состоять из нескольких букв).
     */

    public Variable(String name) {
        this.name = name;
    }


    @Override
    public void print() {
        System.out.print(name);
    }

    @Override
    public Expression derivative(String variable) {
        // Производная переменной по самой себе - 1, по любой другой - 0
        if (name.equals(variable)) {
            return new Number(1);
        }
        return new Number(0);
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        if (!values.containsKey(name)) {
            throw new IllegalArgumentException("Нет значения переменной " + name);
        }
        return values.get(name);
    }
}