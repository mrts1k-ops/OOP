package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;


class VariableTest {


    @Test
    void printsName() {
        assertEquals("x", ExpressionTestUtils.printToString(new Variable("x")));
    }

    @Test
    void printsMultiLetterName() {
        assertEquals("abc", ExpressionTestUtils.printToString(new Variable("abc")));
    }

    @Test
    void derivativeBySameVariableIsOne() {
        Expression derivative = new Variable("x").derivative("x");
        assertEquals("1", ExpressionTestUtils.printToString(derivative));
    }

    @Test
    void derivativeByOtherVariableIsZero() {
        Expression derivative = new Variable("x").derivative("y");
        assertEquals("0", ExpressionTestUtils.printToString(derivative));
    }

    @Test
    void evaluateReturnsValueFromMap() {
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(10, new Variable("x").evaluate(values));
    }

    @Test
    void evaluateWithoutValueThrows() {
        Map<String, Integer> values = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> new Variable("x").evaluate(values));
    }
}