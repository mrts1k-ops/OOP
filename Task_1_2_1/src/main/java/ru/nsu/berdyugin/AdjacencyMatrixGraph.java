package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Граф, хранящийся через матрицу смежности.
 * (квадратная таблица true/false).
 */

public class AdjacencyMatrixGraph implements Graph {
    private final List<Integer> vertices = new ArrayList<>();
    private final List<List<Boolean>> matrix = new ArrayList<>();


    @Override
    public void addVertex(int vertex) {
        if (vertices.contains(vertex)) {
            throw new IllegalArgumentException("Вершина " + vertex + " уже есть в графе");
        }
        for (List<Boolean> row : matrix) {
            row.add(false);
        }
        List<Boolean> newRow = new ArrayList<>();
        for (int i = 0; i < vertices.size() + 1; i++) {
            newRow.add(false);
        }
        matrix.add(newRow);
        vertices.add(vertex);
    }

    @Override
    public void removeVertex(int vertex) {
        int index = indexOf(vertex);
        vertices.remove(index);
        matrix.remove(index);
        for (List<Boolean> row : matrix) {
            row.remove(index);
        }
    }

    @Override
    public void addEdge(int from, int to) {
        matrix.get(indexOf(from)).set(indexOf(to), true);
    }

    @Override
    public void removeEdge(int from, int to) {
        matrix.get(indexOf(from)).set(indexOf(to), false);
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Boolean> row = matrix.get(indexOf(vertex));
        List<Integer> neighbors = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (row.get(j)) {
                neighbors.add(vertices.get(j));
            }
        }
        Collections.sort(neighbors);
        return neighbors;
    }

    @Override
    public List<Integer> getVertices() {
        return new ArrayList<>(vertices);
    }

    // Находит позицию вершины в vertices
    private int indexOf(int vertex) {
        int index = vertices.indexOf(vertex);
        if (index == -1) {
            throw new IllegalArgumentException("Вершины " + vertex + " нет в графе");
        }
        return index;
    }

    @Override
    public boolean equals(Object other) {
        return GraphUtils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return GraphUtils.hashCode(this);
    }

    @Override
    public String toString() {
        return GraphUtils.toString(this);
    }
}