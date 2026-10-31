package ru.nsu.berdyugin;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Читает граф из файла фиксированного формата.
 * (первая строка - количество вершин, дальше по одной строке на ребро вида "откуда куда").
 */


public class GraphReader {

    private GraphReader() {
    }


    /**
     * Читает граф из файла и заполняет переданный пустой граф.
     *
     * @param graph пустой граф, который нужно заполнить.
     * @param path путь к файлу.
     * @throws IOException если файл не удалось прочитать.
     */

    public static void readFromFile(Graph graph, String path) throws IOException {
        Path file = Paths.get(path);
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Файл не найден: " + path);
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        readFromLines(graph, lines);
    }


    static void readFromLines(Graph graph, List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой: нет количества вершин");
        }

        int vertexCount = parseVertexCount(lines.get(0));
        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(i);
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            parseEdge(graph, line, i + 1);
        }
    }


    // Разбирает первую строку (количество вершин)
    private static int parseVertexCount(String line) {
        int vertexCount;
        try {
            vertexCount = Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Первая строка должна быть числом вершин, а не \"" + line + "\"");
        }
        if (vertexCount < 0) {
            throw new IllegalArgumentException(
                    "Количество вершин не может быть отрицательным: " + vertexCount);
        }
        return vertexCount;
    }


    // Разбирает строку ребра "откуда куда" и добавляет
    private static void parseEdge(Graph graph, String line, int lineNumber) {
        String[] parts = line.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Строка " + lineNumber
                    + " должна содержать два числа \"откуда куда\": \"" + line + "\"");
        }

        int from = parseVertex(parts[0], lineNumber);
        int to = parseVertex(parts[1], lineNumber);
        graph.addEdge(from, to);
    }


    // Превращает текст в номер вершины
    private static int parseVertex(String text, int lineNumber) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Строка " + lineNumber + " содержит не число: \"" + text + "\"");
        }
    }
}