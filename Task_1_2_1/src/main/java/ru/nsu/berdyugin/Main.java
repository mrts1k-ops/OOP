package ru.nsu.berdyugin;


import java.io.IOException;
import java.util.List;


/**
 * Демонстрация работы с графом.
 * (три способа хранения, равенство между ними, топологическая сортировка и чтение из файла).
 */


public class Main {

    public static void main(String[] args) throws IOException {
        // Один и тот же граф в разных представлениях
        Graph matrix = new AdjacencyMatrixGraph();
        Graph incidence = new IncidenceMatrixGraph();
        Graph list = new AdjacencyListGraph();

        for (Graph graph : new Graph[]{matrix, incidence, list}) {
            graph.addVertex(0);
            graph.addVertex(1);
            graph.addVertex(2);
            graph.addVertex(3);
            graph.addEdge(0, 1);
            graph.addEdge(0, 2);
            graph.addEdge(1, 3);
            graph.addEdge(2, 3);
        }

        System.out.println(matrix);
        System.out.println();

        // Разные представления одного и того же графа (равны)
        System.out.println("matrix.equals(incidence): " + matrix.equals(incidence));
        System.out.println("matrix.equals(list): " + matrix.equals(list));

        // Топологическая сортировка работает одинаково для всех
        System.out.println("Топологический порядок: " + TopologicalSort.sort(matrix));

        Graph fromFile = new AdjacencyListGraph();
        GraphReader.readFromFile(fromFile, "graph.txt");
        System.out.println("Граф из файла: " + fromFile);
    }
}