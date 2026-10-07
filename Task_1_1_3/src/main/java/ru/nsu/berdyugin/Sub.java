package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Разность двух выражений: left - right.
 */

public class Sub extends Expression {
    private final Expression left; // Уменьшаемое
    private final Expression right; // Вычитаемое


    /**
     * Создаёт разность.
     *
     * @param left уменьшаемое.
     * @param right вычитаемое.
     */

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("-");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variable) {
        // (f - g)' = f' - g'
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return left.evaluate(values) - right.evaluate(values);
    }
}