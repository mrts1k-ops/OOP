package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;


class AddTest {

    @Test
    void printsWithPlusSign() {
        Expression add = new Add(new Number(3), new Variable("x"));
        assertEquals("(3+x)", ExpressionTestUtils.printToString(add));
    }

    @Test
    void evaluatesSum() {
        Expression add = new Add(new Number(2), new Number(3));
        assertEquals(5, add.evaluate(new HashMap<>()));
    }

    @Test
    void derivativeIsSumOfDerivatives() {
        Expression add = new Add(new Variable("x"), new Number(5));
        Expression derivative = add.derivative("x");
        assertEquals("(1+0)", ExpressionTestUtils.printToString(derivative));
    }
}