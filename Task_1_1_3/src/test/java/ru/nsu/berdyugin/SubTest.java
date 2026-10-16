package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;


class SubTest {


    @Test
    void printsWithMinusSign() {
        Expression sub = new Sub(new Number(5), new Variable("x"));
        assertEquals("(5-x)", ExpressionTestUtils.printToString(sub));
    }

    @Test
    void evaluatesDifference() {
        Expression sub = new Sub(new Number(5), new Number(3));
        assertEquals(2, sub.evaluate(new HashMap<>()));
    }

    @Test
    void derivativeIsDifferenceOfDerivatives() {
        Expression sub = new Sub(new Variable("x"), new Number(5));
        Expression derivative = sub.derivative("x");
        assertEquals("(1-0)", ExpressionTestUtils.printToString(derivative));
    }
}