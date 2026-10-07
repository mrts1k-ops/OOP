package ru.nsu.berdyugin;


import java.util.HashMap;
import java.util.Map;


/**
 * Базовый класс математического выражения. У каждого выражения можно
 * напечатать его запись, продифференцировать по переменной и вычислить
 * значение при известных значениях переменных.
 */

public abstract class Expression {


    /**
     * Печатает выражение в консоль по правилам из задания:
     * числа и переменные без скобок, остальное всегда в скобках.
     */

    public abstract void print();


    /**
     * Строит новое выражение - производную этого выражения по заданной переменной.
     * Исходное выражение (this) при этом не меняется.
     *
     * @param variable имя переменной, по которой дифференцируем.
     * @return новое выражение-производная.
     */

    public abstract Expression derivative(String variable);


    /**
     * Вычисляет значение выражения при уже известных значениях переменных.
     * Нужен отдельно от eval(), чтобы не разбирать строку с означиванием
     * заново на каждом узле дерева выражения.
     *
     * @param values значения переменных по их именам.
     * @return результат вычисления.
     */

    public abstract int evaluate(Map<String, Integer> values);


    /**
     * Вычисляет значение выражения по строке означивания переменных
     * вида "x = 10; y = 13".
     *
     * @param assignments строка со значениями переменных через ";".
     * @return результат вычисления.
     */

    public int eval(String assignments) {
        Map<String, Integer> values = parseAssignments(assignments);
        return evaluate(values);
    }

    // Разбирает строку "x = 10; y = 13" в карту имя -> значение
    private static Map<String, Integer> parseAssignments(String assignments) {
        Map<String, Integer> values = new HashMap<>();
        String[] parts = assignments.split(";");
        for (int i = 0; i < parts.length; i++) {
            String[] nameAndValue = parts[i].split("=");
            String name = nameAndValue[0].trim();
            int value = Integer.parseInt(nameAndValue[1].trim());
            values.put(name, value);
        }
        return values;
    }
}