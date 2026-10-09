package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;


class DivTest {

    @Test
    void printsWithSlashSign() {
        Expression div = new Div(new Variable("x"), new Number(2));
        assertEquals("(x/2)", ExpressionTestUtils.printToString(div));
    }

    @Test
    void evaluatesQuotient() {
        Expression div = new Div(new Number(6), new Number(2));
        assertEquals(3, div.evaluate(new HashMap<>()));
    }

    @Test
    void evaluatesIntegerDivision() {
        Expression div = new Div(new Number(7), new Number(2));
        assertEquals(3, div.evaluate(new HashMap<>())); // 7 / 2 = 3, остаток отбрасывается
    }

    @Test
    void derivativeUsesQuotientRule() {
        // (x/2)' = ((1*2)-(x*0)) / (2*2)
        Expression div = new Div(new Variable("x"), new Number(2));
        Expression derivative = div.derivative("x");
        assertEquals("(((1*2)-(x*0))/(2*2))", ExpressionTestUtils.printToString(derivative));
    }
}