package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;


class NumberTest {


    @Test
    void printsValue() {
        assertEquals("5", ExpressionTestUtils.printToString(new Number(5)));
    }

    @Test
    void derivativeIsAlwaysZero() {
        Expression derivative = new Number(5).derivative("x");
        assertEquals("0", ExpressionTestUtils.printToString(derivative));
    }

    @Test
    void evaluateReturnsValue() {
        assertEquals(5, new Number(5).evaluate(new HashMap<>()));
    }

    @Test
    void evaluateIgnoresVariables() {
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 100);
        assertEquals(5, new Number(5).evaluate(values)); // Константа не зависит от переменных
    }
}