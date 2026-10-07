package ru.nsu.berdyugin;

import java.util.Map;


/**
 * Частное двух выражений: left / right.
 */

public class Div extends Expression {
    private final Expression left; // Делимое
    private final Expression right; // Делитель


    /**
     * Создаёт частное.
     *
     * @param left делимое.
     * @param right делитель.
     */

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public void print() {
        System.out.print("(");
        left.print();
        System.out.print("/");
        right.print();
        System.out.print(")");
    }

    @Override
    public Expression derivative(String variable) {
        // Правило частного: (f / g)' = (f' * g - f * g') / (g * g)
        Expression numerator = new Sub(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
        Expression denominator = new Mul(right, right);
        return new Div(numerator, denominator);
    }

    @Override
    public int evaluate(Map<String, Integer> values) {
        return left.evaluate(values) / right.evaluate(values);
    }
}