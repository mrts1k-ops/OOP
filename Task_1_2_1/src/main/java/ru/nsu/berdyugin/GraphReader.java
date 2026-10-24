package ru.nsu.berdyugin;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        readFromLines(graph, lines);
    }


    // Отдельно от чтения файла (не нужен реальный файл на диске)
    static void readFromLines(Graph graph, List<String> lines) {
        int vertexCount = Integer.parseInt(lines.get(0).trim());
        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(i);
        }

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);
            graph.addEdge(from, to);
        }
    }
}