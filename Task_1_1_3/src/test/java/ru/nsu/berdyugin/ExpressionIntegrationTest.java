package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


/**
 * Проверяет пример из условия задания целиком: создание выражения,
 * печать, дифференцирование и вычисление значения.
 */

class ExpressionIntegrationTest {

    @Test
    void exampleFromTaskWorks() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        assertEquals("(3+(2*x))", ExpressionTestUtils.printToString(e));

        Expression de = e.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", ExpressionTestUtils.printToString(de));

        assertEquals(23, e.eval("x = 10; y = 13"));
    }


    @Test
    void originalExpressionDoesNotChangeAfterDerivative() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        e.derivative("x"); // Результат не сохраняем - проверяем, что e не изменилось
        assertEquals("(3+(2*x))", ExpressionTestUtils.printToString(e));
    }


    @Test
    void parserProducesSameExpressionAsManualConstruction() {
        Expression manual = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        Expression parsed = ExpressionParser.parse("(3+(2*x))");
        assertEquals(ExpressionTestUtils.printToString(manual), ExpressionTestUtils.printToString(parsed));
    }
}