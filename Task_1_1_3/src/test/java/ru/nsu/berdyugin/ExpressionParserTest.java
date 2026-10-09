package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class ExpressionParserTest {


    @Test
    void parsesNumber() {
        Expression result = ExpressionParser.parse("42");
        assertEquals("42", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesVariable() {
        Expression result = ExpressionParser.parse("x");
        assertEquals("x", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesMultiLetterVariable() {
        Expression result = ExpressionParser.parse("abc");
        assertEquals("abc", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesAddition() {
        Expression result = ExpressionParser.parse("(3+2)");
        assertEquals("(3+2)", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesSubtraction() {
        Expression result = ExpressionParser.parse("(3-2)");
        assertEquals("(3-2)", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesMultiplication() {
        Expression result = ExpressionParser.parse("(3*2)");
        assertEquals("(3*2)", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesDivision() {
        Expression result = ExpressionParser.parse("(3/2)");
        assertEquals("(3/2)", ExpressionTestUtils.printToString(result));
    }

    @Test
    void parsesNestedExpression() {
        Expression result = ExpressionParser.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", ExpressionTestUtils.printToString(result));
    }

    @Test
    void ignoresSpacesInsideExpression() {
        Expression result = ExpressionParser.parse("(3 + (2 * x))");
        assertEquals("(3+(2*x))", ExpressionTestUtils.printToString(result)); // Пробелы не попадают в вывод
    }

    @Test
    void parsedExpressionEvaluatesCorrectly() {
        Expression result = ExpressionParser.parse("(3+(2*x))");
        assertEquals(23, result.eval("x = 10"));
    }
}