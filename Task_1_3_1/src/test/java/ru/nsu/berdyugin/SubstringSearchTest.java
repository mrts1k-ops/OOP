package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class SubstringSearchTest {
    private static class GeneratedReader extends Reader {
        static final String MARKER = "МЕТКА";
        private final long totalChars;
        private final long period;
        private long position = 0;
        private long inPeriod = 0;

        GeneratedReader(long totalChars, long period) {
            this.totalChars = totalChars;
            this.period = period;
        }

        @Override
        public int read(char[] buffer, int offset, int length) {
            if (position >= totalChars) {
                return -1;
            }
            int count = (int) Math.min(length, totalChars - position);
            int filled = 0;
            while (filled < count) {
                if (inPeriod < MARKER.length()) {
                    buffer[offset + filled] = MARKER.charAt((int) inPeriod);
                    filled++;
                    inPeriod++;
                } else {
                    int run = (int) Math.min(count - filled, period - inPeriod);
                    Arrays.fill(buffer, offset + filled, offset + filled + run, 'ж');
                    filled += run;
                    inPeriod += run;
                }
                if (inPeriod == period) {
                    inPeriod = 0;
                }
            }
            position += count;
            return count;
        }

        @Override
        public void close() {
        }
    }

    private List<Long> search(String text, String pattern) throws IOException {
        return SubstringSearch.find(new StringReader(text), pattern);
    }

    private Path fileWith(String text) throws IOException {
        Path file = Files.createTempFile("search", ".txt");
        Files.write(file, text.getBytes(StandardCharsets.UTF_8));
        return file;
    }


    // Пример из условия задания.

    @Test
    void exampleFromTask() throws IOException {
        Path file = fileWith("абракадабра");
        try {
            assertEquals(Arrays.asList(1L, 8L), SubstringSearch.find(file.toString(), "бра"));
        } finally {
            Files.delete(file);
        }
    }


    // Обычные случаи.

    @Test
    void findsAllOccurrences() throws IOException {
        assertEquals(Arrays.asList(1L, 8L), search("абракадабра", "бра"));
    }

    @Test
    void patternNotFoundGivesEmptyList() throws IOException {
        assertTrue(search("абракадабра", "xyz").isEmpty());
    }

    @Test
    void patternAtTheStart() throws IOException {
        assertEquals(Arrays.asList(0L), search("абракадабра", "абрак"));
    }

    @Test
    void patternAtTheEnd() throws IOException {
        assertEquals(Arrays.asList(4L), search("абракадабра", "кадабра"));
    }

    @Test
    void patternEqualsWholeText() throws IOException {
        assertEquals(Arrays.asList(0L), search("текст", "текст"));
    }

    @Test
    void patternLongerThanTextGivesEmptyList() throws IOException {
        assertTrue(search("ab", "abc").isEmpty());
    }

    @Test
    void singleCharacterPattern() throws IOException {
        assertEquals(Arrays.asList(0L, 3L, 5L, 7L, 10L), search("абракадабра", "а"));
    }

    @Test
    void emptyTextGivesEmptyList() throws IOException {
        assertTrue(search("", "a").isEmpty());
    }

    @Test
    void searchIsCaseSensitive() throws IOException {
        assertTrue(search("Привет", "привет").isEmpty());
    }

    @Test
    void patternMayContainNewline() throws IOException {
        assertEquals(Arrays.asList(1L), search("ab\ncd", "b\nc"));
    }


    // Пересекающиеся вхождения.

    @Test
    void overlappingOccurrencesAreAllFound() throws IOException {
        assertEquals(Arrays.asList(0L, 1L, 2L), search("аааа", "аа"));
        assertEquals(Arrays.asList(0L, 2L, 4L), search("abababa", "aba"));
    }


    // Случаи, на которых простой алгоритм с откатом "в начало" ошибается.

    @Test
    void falseStartsDoNotBreakSearch() throws IOException {
        assertEquals(Arrays.asList(2L), search("aaaaab", "aaab"));
        assertEquals(Arrays.asList(3L), search("abcabcabcd", "abcabcd"));
        assertEquals(Arrays.asList(4L), search("ababababc", "ababc"));
    }


    // Разные алфавиты.

    @Test
    void cyrillic() throws IOException {
        assertEquals(Arrays.asList(7L), search("Привет мир", "мир"));
    }

    @Test
    void greek() throws IOException {
        assertEquals(Arrays.asList(0L, 9L), search("καλημέρα καλά", "κα"));
    }

    @Test
    void chinese() throws IOException {
        assertEquals(Arrays.asList(0L, 4L), search("你好世界你好", "你好"));
    }

    @Test
    void arabic() throws IOException {
        assertEquals(Arrays.asList(6L), search("مرحبا بالعالم", "بال"));
    }


    // Символы, которые в Java занимают две позиции char, считаются как один.

    @Test
    void emojiCountsAsOneCharacter() throws IOException {
        assertEquals(Arrays.asList(0L, 2L, 4L), search("😀a😀b😀", "😀"));
    }

    @Test
    void patternWithEmojiInside() throws IOException {
        assertEquals(Arrays.asList(1L, 3L), search("a😀b😀b", "😀b"));
    }

    @Test
    void lonelyHalfOfPairDoesNotBreakSearch() throws IOException {
        assertEquals(Arrays.asList(1L), search("\uD83Da", "a"));
        assertEquals(Arrays.asList(0L), search("a\uD83D", "a"));
        assertEquals(Arrays.asList(1L), search("a\uD83D", "\uD83D"));
    }

    @Test
    void pairSplitBetweenTwoReadChunksIsFound() throws IOException {
        String text = "x".repeat(SubstringSearch.BUFFER_SIZE - 1) + "😀" + "x";
        assertEquals(Arrays.asList((long) SubstringSearch.BUFFER_SIZE - 1),
                search(text, "😀"));
    }

    @Test
    void occurrenceSplitBetweenTwoReadChunksIsFound() throws IOException {
        String text = "x".repeat(SubstringSearch.BUFFER_SIZE - 2) + "abcd" + "x";
        assertEquals(Arrays.asList((long) SubstringSearch.BUFFER_SIZE - 2),
                search(text, "abcd"));
    }


    // Чтение из файла.

    @Test
    void multiByteFileIsDecodedAsUtf8() throws IOException {
        Path file = fileWith("Привет, мир! 你好 😀 мир");
        try {
            assertEquals(Arrays.asList(8L, 18L), SubstringSearch.find(file.toString(), "мир"));
        } finally {
            Files.delete(file);
        }
    }

    @Test
    void biggerFileIsSearchedFromDisk() throws IOException {
        String filler = "ж".repeat(70);
        Path file = Files.createTempFile("big", ".txt");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                for (int i = 0; i < 100000; i++) {
                    writer.write(filler);
                    writer.write(i % 1000 == 0 ? "НАЙДИ" : "ножно");
                }
            }
            List<Long> found = SubstringSearch.find(file.toString(), "НАЙДИ");
            assertEquals(100, found.size());
            assertEquals(Long.valueOf(70), found.get(0));
            assertEquals(Long.valueOf(75L * 1000 + 70), found.get(1));
        } finally {
            Files.delete(file);
        }
    }

    @Test
    void hugeTextIsSearchedWithoutKeepingItInMemory() throws IOException {
        long period = 500_000_000L;
        long total = 2_600_000_000L;

        List<Long> found = SubstringSearch.find(new GeneratedReader(total, period), "МЕТКА");

        assertEquals(Arrays.asList(0L, period, 2 * period, 3 * period, 4 * period, 5 * period),
                found);
        assertTrue(found.get(5) > Integer.MAX_VALUE);
    }


    // Неправильные аргументы.

    @Test
    void emptyPatternThrows() {
        assertThrows(IllegalArgumentException.class, () -> search("abc", ""));
    }

    @Test
    void nullPatternThrows() {
        assertThrows(IllegalArgumentException.class, () -> search("abc", null));
    }

    @Test
    void nullFileNameThrows() {
        String fileName = null;
        assertThrows(IllegalArgumentException.class, () -> SubstringSearch.find(fileName, "a"));
    }

    @Test
    void missingFileThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> SubstringSearch.find("no_such_file.txt", "a"));
    }
}