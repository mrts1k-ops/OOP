package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


/**
 * Топологическая сортировка вершин графа алгоритмом Кана.
 * (Работает через интерфейс Graph, поэтому одинаково подходит для любого способа хранения).
 */


public class TopologicalSort {

    /**
     * Строит топологический порядок вершин графа.
     *
     * @param graph граф, который нужно отсортировать.
     * @return вершины в топологическом порядке.
     */

    public static List<Integer> sort(Graph graph) {
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int vertex : graph.getVertices()) {
            inDegree.put(vertex, 0);
        }
        for (int vertex : graph.getVertices()) {
            for (int neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }


        // TreeSet - чтобы всегда брать вершину с наименьшим номером
        TreeSet<Integer> available = new TreeSet<>();
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                available.add(entry.getKey());
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!available.isEmpty()) {
            int vertex = available.pollFirst();
            result.add(vertex);

            for (int neighbor : graph.getNeighbors(vertex)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    available.add(neighbor);
                }
            }
        }


        if (result.size() != graph.getVertices().size()) {
            // Проверяем наличие цикла
            throw new IllegalStateException(
                    "В графе есть цикл - топологическая сортировка невозможна");
        }
        return result;
    }
}