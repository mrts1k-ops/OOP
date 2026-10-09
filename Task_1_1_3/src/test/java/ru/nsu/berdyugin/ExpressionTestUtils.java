package ru.nsu.berdyugin;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


/**
 * Вспомогательный класс для тестов: печатает выражение не в консоль,
 * а в строку, чтобы можно было сравнить результат через assertEquals.
 */

class ExpressionTestUtils {

    // Подменяет System.out на время вызова print(), возвращает напечатанный текст
    static String printToString(Expression expression) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            expression.print();
        } finally {
            System.setOut(oldOut); // Возвращаем обычную консоль
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}