package ru.nsu.berdyugin;


/**
 * Демонстрация работы с иерархией математических выражений.
 */

public class Main {

    public static void main(String[] args) {
        
        // (3+(2*x))
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        e.print();
        System.out.println();

        // Дифференцируем по x, исходное выражение e при этом не меняется
        Expression de = e.derivative("x");
        de.print();
        System.out.println();

        // Вычисляем значение при x = 10 ("y" не используется в выражении)
        int result = e.eval("x = 10; y = 13");
        System.out.println(result);

        // Создание выражения из строки
        Expression parsed = ExpressionParser.parse("(3+(2*x))");
        parsed.print();
        System.out.println();
    }
}