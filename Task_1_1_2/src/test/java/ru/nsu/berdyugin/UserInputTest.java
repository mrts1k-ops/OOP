package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;
import org.junit.jupiter.api.Test;


class UserInputTest {


    @Test
    void oneMeansYes() {
        UserInput input = new UserInput(new Scanner("1\n"));
        assertTrue(input.askYesNo("Вопрос?"));
    }


    @Test
    void zeroMeansNo() {
        UserInput input = new UserInput(new Scanner("0\n"));
        assertFalse(input.askYesNo("Вопрос?"));
    }


    @Test
    void spacesAreIgnored() {
        UserInput input = new UserInput(new Scanner("  1  \n"));
        assertTrue(input.askYesNo("Вопрос?"));
    }


    @Test
    void wrongInputAsksAgain() {
        UserInput input = new UserInput(new Scanner("5\nabc\n\n0\n"));
        assertFalse(input.askYesNo("Вопрос?"));
    }


    @Test
    void wrongInputThenOne() {
        UserInput input = new UserInput(new Scanner("2\nда\n1\n"));
        assertTrue(input.askYesNo("Вопрос?"));
    }
}