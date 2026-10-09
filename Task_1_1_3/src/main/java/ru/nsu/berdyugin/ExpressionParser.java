package ru.nsu.berdyugin;


/**
 * Разбирает выражение из строки в дерево объектов Expression.
 * Числа и переменные без скобок, остальные выражения всегда
 * в скобках вида "(левое_выражение операция правое_выражение)".
 */

public class ExpressionParser {
    private final String text; // Строка, которую разбираем
    private int pos; // Текущая позиция чтения

    private ExpressionParser(String text) {
        this.text = text;
        this.pos = 0;
    }


    /**
     * Разбирает строку в выражение.
     *
     * @param text строка с выражением, например "(3+(2*x))".
     * @return дерево выражения.
     */

    public static Expression parse(String text) {
        ExpressionParser parser = new ExpressionParser(text);
        return parser.parseExpression();
    }

    // Разбирает одно выражение: число, переменную или выражение в скобках
    private Expression parseExpression() {
        char c = peek();
        if (c == '(') {
            return parseBinary();
        }
        if (Character.isDigit(c)) {
            return parseNumber();
        }
        return parseVariable();
    }

    // Разбирает "(левое операция правое)"
    private Expression parseBinary() {
        expect('(');
        Expression left = parseExpression();
        char operator = next();
        Expression right = parseExpression();
        expect(')');

        if (operator == '+') {
            return new Add(left, right);
        }
        if (operator == '-') {
            return new Sub(left, right);
        }
        if (operator == '*') {
            return new Mul(left, right);
        }
        if (operator == '/') {
            return new Div(left, right);
        }
        throw new IllegalArgumentException("Неизвестная операция: " + operator);
    }

    // Разбирает число из подряд идущих цифр
    private Expression parseNumber() {
        int start = pos;
        while (pos < text.length() && Character.isDigit(text.charAt(pos))) {
            pos++;
        }
        String digits = text.substring(start, pos);
        return new Number(Integer.parseInt(digits));
    }

    // Разбирает имя переменной из подряд идущих букв
    private Expression parseVariable() {
        int start = pos;
        while (pos < text.length() && Character.isLetter(text.charAt(pos))) {
            pos++;
        }
        String name = text.substring(start, pos);
        return new Variable(name);
    }

    // Пропускает пробелы перед текущим символом
    private void skipSpaces() {
        while (pos < text.length() && text.charAt(pos) == ' ') {
            pos++;
        }
    }

    // Смотрит на текущий символ, не сдвигая позицию
    private char peek() {
        skipSpaces();
        return text.charAt(pos);
    }

    // Возвращает текущий символ и сдвигает позицию на один вперёд
    private char next() {
        skipSpaces();
        char c = text.charAt(pos);
        pos++;
        return c;
    }

    // Проверяет, что текущий символ ожидаемый, и сдвигает позицию
    private void expect(char expected) {
        skipSpaces();
        if (text.charAt(pos) != expected) {
            throw new IllegalArgumentException(
                    "Ожидался символ '" + expected + "' на позиции " + pos);
        }
        pos++;
    }
}
