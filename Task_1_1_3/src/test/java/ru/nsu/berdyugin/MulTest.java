package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;


class MulTest {


    @Test
    void printsWithTimesSign() {
        Expression mul = new Mul(new Number(2), new Variable("x"));
        assertEquals("(2*x)", ExpressionTestUtils.printToString(mul));
    }

    @Test
    void evaluatesProduct() {
        Expression mul = new Mul(new Number(2), new Number(3));
        assertEquals(6, mul.evaluate(new HashMap<>()));
    }

    @Test
    void derivativeUsesProductRule() {
        // (2*x)' = (2'*x) + (2*x') = (0*x)+(2*1)
        Expression mul = new Mul(new Number(2), new Variable("x"));
        Expression derivative = mul.derivative("x");
        assertEquals("((0*x)+(2*1))", ExpressionTestUtils.printToString(derivative));
    }
}