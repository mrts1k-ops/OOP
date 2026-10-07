package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Сумма двух выражений: left + right.
 */

public class Add extends Expression {
    private final Expression left; // Левое слагаемое
    private final Expression right; // Правое слагаемое


    /**
     * Создаёт сумму.
     *
     * @param left левое слагаемое.
     * @param right правое слагаемое.
     */

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("+");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variable) {
        // (f + g)' = f' + g'
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return left.evaluate(values) + right.evaluate(values);
    }
}