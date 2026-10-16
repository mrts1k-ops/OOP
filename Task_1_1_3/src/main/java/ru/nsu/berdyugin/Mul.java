package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Произведение двух выражений (left * right).
 */

public class Mul extends Expression {
    private final Expression left; // Левый множитель
    private final Expression right; // Правый множитель


    /**
     * Создаёт произведение.
     *
     * @param left левый множитель.
     * @param right правый множитель.
     */

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("*");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variable) {
        // Правило произведения: (f * g)' = f' * g + f * g'
        Expression firstPart = new Mul(left.derivative(variable), right);
        Expression secondPart = new Mul(left, right.derivative(variable));
        return new Add(firstPart, secondPart);
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return left.evaluate(values) * right.evaluate(values);
    }
}