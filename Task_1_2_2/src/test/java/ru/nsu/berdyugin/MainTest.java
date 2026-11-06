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
        assertTrue(output.startsWith("1.0"));
        assertTrue(output.contains("Есть ключ two: true"));
        assertTrue(output.contains("Таблицу нельзя менять во время обхода"));
        assertTrue(output.contains("После удаления:"));
    }
}