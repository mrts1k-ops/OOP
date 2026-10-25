package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;


class MainTest {

    @Test
    void mainRuns() throws IOException {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[]{});
        } finally {
            System.setOut(oldOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Вершины: [0, 1, 2, 3]"));
        assertTrue(output.contains("Рёбра:"));
        assertTrue(output.contains("matrix.equals(incidence): true"));
        assertTrue(output.contains("matrix.equals(list): true"));
        assertTrue(output.contains("Топологический порядок: [0, 1, 2, 3]"));
        assertTrue(output.contains("Граф из файла:"));
    }
}