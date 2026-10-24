package ru.nsu.berdyugin;


import java.util.List;


/**
 * Ориентированный граф на вершинах-числах.
 */

public interface Graph {


    /**
     * Добавляет вершину в граф.
     *
     * @param vertex номер вершины.
     */

    void addVertex(int vertex);


    /**
     * Удаляет вершину и все рёбра которые её касаются.
     *
     * @param vertex номер вершины.
     */

    void removeVertex(int vertex);


    /**
     * Добавляет ориентированное ребро from -> to.
     *
     * @param from вершина, откуда идёт ребро.
     * @param to вершина, куда идёт ребро.
     */

    void addEdge(int from, int to);


    /**
     * Удаляет ребро from -> to, если оно есть.
     *
     * @param from вершина, откуда идёт ребро.
     * @param to вершина, куда идёт ребро.
     */

    void removeEdge(int from, int to);


    /**
     * Возвращает соседей вершины (те, в которые есть исходящее ребро).
     *
     * @param vertex номер вершины.
     * @return список соседей, отсортированный по возрастанию.
     */

    List<Integer> getNeighbors(int vertex);


    /**
     * Возвращает все вершины графа.
     *
     * @return список вершин.
     */

    List<Integer> getVertices();
}