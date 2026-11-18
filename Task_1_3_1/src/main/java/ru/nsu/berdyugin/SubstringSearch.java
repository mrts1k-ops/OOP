package ru.nsu.berdyugin;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Поиск всех вхождений подстроки в тексте, который может быть намного больше
 * оперативной памяти. Текст читается кусками и никогда не хранится целиком
 * (в памяти лежат только один кусок, искомая строка и найденные индексы).
 */

public class SubstringSearch {
    static final int BUFFER_SIZE = 65536;

    private SubstringSearch() {
    }


    /**
     * Ищет подстроку в файле в кодировке UTF-8.
     *
     * @param fileName имя файла.
     * @param pattern искомая строка.
     * @return индексы начала каждого вхождения (с нуля, в символах, а не в байтах),
     *         по возрастанию. Вхождения могут перекрываться.
     * @throws IOException если файл не удалось прочитать.
     * @throws IllegalArgumentException если имя файла или строка не заданы,
     *         строка пустая или файла нет.
     */

    public static List<Long> find(String fileName, String pattern) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("Имя файла не задано");
        }
        if (!Files.exists(Paths.get(fileName))) {
            throw new IllegalArgumentException("Файл не найден: " + fileName);
        }
        try (Reader reader = new InputStreamReader(
                new FileInputStream(fileName), StandardCharsets.UTF_8)) {
            return find(reader, pattern);
        }
    }


    /**
     * Ищет подстроку в тексте, который отдаёт Reader. Отдельно от работы с файлом
     * (так поиск можно проверять и на строках, и на данных, созданных на лету).
     *
     * @param reader источник текста.
     * @param pattern искомая строка.
     * @return индексы начала каждого вхождения (с нуля, в символах), по возрастанию.
     * @throws IOException если текст не удалось прочитать.
     * @throws IllegalArgumentException если строка не задана или пустая.
     */

    public static List<Long> find(Reader reader, String pattern) throws IOException {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Искомая строка не может быть пустой");
        }

        Searcher searcher = new Searcher(toCodePoints(pattern));
        char[] buffer = new char[BUFFER_SIZE];
        char pendingHigh = 0; // Первая половина пары, ждущая вторую из следующего куска.
        boolean hasPending = false;

        int count = reader.read(buffer);
        while (count != -1) {
            for (int i = 0; i < count; i++) {
                char current = buffer[i];
                if (hasPending) {
                    hasPending = false;
                    if (Character.isLowSurrogate(current)) {
                        searcher.accept(Character.toCodePoint(pendingHigh, current));
                        continue;
                    }
                    searcher.accept(pendingHigh);
                }
                if (Character.isHighSurrogate(current)) {
                    pendingHigh = current; // Ждём вторую половину.
                    hasPending = true;
                } else {
                    searcher.accept(current);
                }
            }
            count = reader.read(buffer);
        }
        if (hasPending) {
            searcher.accept(pendingHigh); // Текст закончился на одинокой половине пары.
        }
        return searcher.getResult();
    }


    // Превращает строку в массив символов Unicode (смайлики, редкие
    // иероглифы занимают две позиции типа char, но это один символ).
    private static int[] toCodePoints(String text) {
        int[] points = new int[text.codePointCount(0, text.length())];
        int index = 0;
        int i = 0;
        while (i < text.length()) {
            int codePoint = text.codePointAt(i);
            points[index] = codePoint;
            index++;
            i += Character.charCount(codePoint);
        }
        return points;
    }

    // Принимает символы текста по одному и запоминает, где закончились вхождения.
    private static class Searcher {
        private final int[] pattern;
        private final int[] prefix;
        private final List<Long> result = new ArrayList<>();
        private int matched = 0;
        private long position = 0;

        private Searcher(int[] pattern) {
            this.pattern = pattern;
            this.prefix = buildPrefixTable(pattern);
        }

        // Обрабатывает следующий символ текста.
        private void accept(int codePoint) {
            while (matched > 0 && codePoint != pattern[matched]) {
                matched = prefix[matched - 1];
            }
            if (codePoint == pattern[matched]) {
                matched++;
            }
            if (matched == pattern.length) {
                result.add(position - pattern.length + 1); // Вхождение закончилось тут.
                matched = prefix[matched - 1];
            }
            position++;
        }

        private List<Long> getResult() {
            return result;
        }

        private static int[] buildPrefixTable(int[] pattern) {
            int[] table = new int[pattern.length];
            int length = 0;
            for (int i = 1; i < pattern.length; i++) {
                while (length > 0 && pattern[i] != pattern[length]) {
                    length = table[length - 1];
                }
                if (pattern[i] == pattern[length]) {
                    length++;
                }
                table[i] = length;
            }
            return table;
        }
    }
}