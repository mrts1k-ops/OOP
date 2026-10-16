package ru.nsu.berdyugin;

import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;


class MainTest {

    @Test
    void mainRuns() {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[]{});
        } finally {
            System.setOut(oldOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("(3+(2*x))"));
        assertTrue(output.contains("23"));
    }
}