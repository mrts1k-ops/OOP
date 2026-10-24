package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Общая логика сравнения и печати графов, одинаковая для любого способа хранения.
 * (два графа равны, если у них одинаковые вершины и одинаковые рёбра).
 */

public final class GraphUtils {

    private GraphUtils() {
    }


    /**
     * Сравнивает граф с другим объектом структурно: по вершинам и рёбрам.
     *
     * @param graph граф, у которого вызван equals().
     * @param other объект, с которым сравниваем.
     * @return true, если other - тоже Graph с теми же вершинами и рёбрами.
     */

    public static boolean equals(Graph graph, Object other) {
        if (!(other instanceof Graph)) {
            return false;
        }
        Graph otherGraph = (Graph) other;
        if (!sameNumbers(graph.getVertices(), otherGraph.getVertices())) {
            return false;
        }
        return sameEdges(allEdges(graph), allEdges(otherGraph));
    }


    /**
     * Хэш-код, согласованный с equals().
     *
     * @param graph граф.
     * @return хэш-код.
     */

    public static int hashCode(Graph graph) {
        int hash = 0;
        for (int vertex : graph.getVertices()) {
            hash += vertex; // Сумма - результат не зависит от порядка вершин
        }
        for (int[] edge : allEdges(graph)) {
            hash += edge[0] * 31 + edge[1]; // Тоже сумма - порядок рёбер не важен
        }
        return hash;
    }


    /**
     * Печатает граф в виде строки: список вершин и список рёбер.
     *
     * @param graph граф.
     * @return строковое представление графа.
     */

    public static String toString(Graph graph) {
        List<Integer> vertices = new ArrayList<>(graph.getVertices());
        Collections.sort(vertices);

        List<int[]> edges = allEdges(graph);
        sortEdges(edges);

        String result = "Вершины: " + vertices + "\nРёбра: [";
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                result += ", ";
            }
            result += "(" + edges.get(i)[0] + "," + edges.get(i)[1] + ")";
        }
        result += "]";
        return result;
    }

    // Собирает все рёбра графа в список пар (откуда, куда)
    private static List<int[]> allEdges(Graph graph) {
        List<int[]> edges = new ArrayList<>();
        for (int vertex : graph.getVertices()) {
            for (int neighbor : graph.getNeighbors(vertex)) {
                edges.add(new int[]{vertex, neighbor});
            }
        }
        return edges;
    }

    // Проверяет что в двух списках чисел одни и те же числа, без учёта порядка
    private static boolean sameNumbers(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int number : a) {
            if (!b.contains(number)) {
                return false;
            }
        }
        return true;
    }

    // Проверяет что в двух списках рёбер одни и те же рёбра, без учёта порядка
    private static boolean sameEdges(List<int[]> a, List<int[]> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int[] edge : a) {
            if (!containsEdge(b, edge)) {
                return false;
            }
        }
        return true;
    }

    // Проверяет есть ли такое ребро в списке
    private static boolean containsEdge(List<int[]> edges, int[] edge) {
        for (int[] e : edges) {
            if (e[0] == edge[0] && e[1] == edge[1]) {
                return true;
            }
        }
        return false;
    }

    // Сортирует рёбра сначала по "откуда", потом по "куда" (выбором)
    private static void sortEdges(List<int[]> edges) {
        for (int i = 0; i < edges.size(); i++) {
            int minIndex = i;
            for (int j = i + 1; j < edges.size(); j++) {
                if (isLess(edges.get(j), edges.get(minIndex))) {
                    minIndex = j;
                }
            }
            int[] temp = edges.get(i);
            edges.set(i, edges.get(minIndex));
            edges.set(minIndex, temp);
        }
    }

    // Сравнивает два ребра: сначала по "откуда", потом по "куда"
    private static boolean isLess(int[] a, int[] b) {
        if (a[0] != b[0]) {
            return a[0] < b[0];
        }
        return a[1] < b[1];
    }
}