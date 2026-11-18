package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;


class MainTest {
    private String runMain(String... args) throws IOException {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(args);
        } finally {
            System.setOut(oldOut);
        }
        return buffer.toString(StandardCharsets.UTF_8).trim();
    }

    @Test
    void withoutArgumentsShowsExampleFromTask() throws IOException {
        assertEquals("[1, 8]", runMain());
    }

    @Test
    void withFileAndPatternSearchesFile() throws IOException {
        Path file = Files.createTempFile("main", ".txt");
        try {
            Files.write(file, "абракадабра".getBytes(StandardCharsets.UTF_8));
            assertEquals("[1, 8]", runMain(file.toString(), "бра"));
        } finally {
            Files.delete(file);
        }
    }

    @Test
    void wrongNumberOfArgumentsShowsUsage() throws IOException {
        assertEquals("Использование: <имя файла> <искомая строка>", runMain("only_one"));
    }
}