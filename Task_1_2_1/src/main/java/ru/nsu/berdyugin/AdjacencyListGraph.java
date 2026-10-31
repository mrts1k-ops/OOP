package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * Граф, хранящийся через список смежности.
 * (у каждой вершины есть список вершин, в которые из неё есть ребро).
 */

public class AdjacencyListGraph extends AbstractGraph {
    private final Map<Integer, List<Integer>> adjacency = new HashMap<>();

    @Override
    public void addVertex(int vertex) {
        if (adjacency.containsKey(vertex)) {
            throw new IllegalArgumentException("Вершина " + vertex + " уже есть в графе");
        }
        adjacency.put(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(int vertex) {
        checkExists(vertex);
        adjacency.remove(vertex);
        for (List<Integer> neighbors : adjacency.values()) {
            neighbors.remove(Integer.valueOf(vertex));
        }
    }

    @Override
    public void addEdge(int from, int to) {
        checkExists(from);
        checkExists(to);
        List<Integer> neighbors = adjacency.get(from);
        if (!neighbors.contains(to)) {
            neighbors.add(to);
        }
    }

    @Override
    public void removeEdge(int from, int to) {
        checkExists(from);
        checkExists(to);
        adjacency.get(from).remove(Integer.valueOf(to));
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        checkExists(vertex);
        List<Integer> neighbors = new ArrayList<>(adjacency.get(vertex));
        Collections.sort(neighbors);
        return neighbors;
    }

    @Override
    public List<Integer> getVertices() {
        return new ArrayList<>(adjacency.keySet());
    }

    // Проверяет что вершина есть в графе
    private void checkExists(int vertex) {
        if (!adjacency.containsKey(vertex)) {
            throw new IllegalArgumentException("Вершины " + vertex + " нет в графе");
        }
    }
}