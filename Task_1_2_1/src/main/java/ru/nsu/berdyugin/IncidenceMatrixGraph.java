package ru.nsu.berdyugin;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Граф хранящийся через матрицу инцидентности: строки - вершины, столбцы - рёбра.
 * (В столбце ребра стоит -1 у вершины, откуда оно выходит, 1 - у вершины)
 */

public class IncidenceMatrixGraph implements Graph {
    private final List<Integer> vertices = new ArrayList<>();
    private final List<List<Integer>> incidence = new ArrayList<>();

    @Override
    public void addVertex(int vertex) {
        if (vertices.contains(vertex)) {
            throw new IllegalArgumentException("Вершина " + vertex + " уже есть в графе");
        }
        int edgeCount = incidence.isEmpty() ? 0 : incidence.get(0).size();
        List<Integer> newRow = new ArrayList<>();
        for (int i = 0; i < edgeCount; i++) {
            newRow.add(0);
        }
        incidence.add(newRow);
        vertices.add(vertex);
    }

    @Override
    public void removeVertex(int vertex) {
        int index = indexOf(vertex);

        // Убираем все рёбра которые касаются этой вершины
        List<Integer> row = incidence.get(index);
        List<Integer> edgesToRemove = new ArrayList<>();
        for (int e = 0; e < row.size(); e++) {
            if (row.get(e) != 0) {
                edgesToRemove.add(e);
            }
        }

        for (int i = edgesToRemove.size() - 1; i >= 0; i--) {
            removeEdgeColumn(edgesToRemove.get(i));
        }

        vertices.remove(index);
        incidence.remove(index);
    }

    @Override
    public void addEdge(int from, int to) {
        int fromIndex = indexOf(from);
        int toIndex = indexOf(to);

        if (hasEdge(fromIndex, toIndex)) {
            return;
        }

        for (List<Integer> row : incidence) {
            row.add(0);
        }
        incidence.get(fromIndex).set(incidence.get(fromIndex).size() - 1, -1);
        incidence.get(toIndex).set(incidence.get(toIndex).size() - 1, 1);
    }

    @Override
    public void removeEdge(int from, int to) {
        int fromIndex = indexOf(from);
        int toIndex = indexOf(to);

        int edgeCount = incidence.isEmpty() ? 0 : incidence.get(0).size();
        for (int e = 0; e < edgeCount; e++) {
            boolean leavesFrom = incidence.get(fromIndex).get(e) == -1;
            boolean entersTo = incidence.get(toIndex).get(e) == 1;
            if (leavesFrom && entersTo) {
                removeEdgeColumn(e);
                return;
            }
        }
    }

    private boolean hasEdge(int fromIndex, int toIndex) {
        int edgeCount = incidence.isEmpty() ? 0 : incidence.get(0).size();
        for (int e = 0; e < edgeCount; e++) {
            boolean leavesFrom = incidence.get(fromIndex).get(e) == -1;
            boolean entersTo = incidence.get(toIndex).get(e) == 1;
            if (leavesFrom && entersTo) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        int vertexIndex = indexOf(vertex);
        List<Integer> neighbors = new ArrayList<>();
        int edgeCount = incidence.isEmpty() ? 0 : incidence.get(0).size();

        for (int e = 0; e < edgeCount; e++) {
            if (incidence.get(vertexIndex).get(e) == -1) {
                for (int r = 0; r < vertices.size(); r++) {
                    if (incidence.get(r).get(e) == 1) {
                        neighbors.add(vertices.get(r));
                        break;
                    }
                }
            }
        }
        Collections.sort(neighbors);
        return neighbors;
    }

    @Override
    public List<Integer> getVertices() {
        return new ArrayList<>(vertices);
    }

    // Убирает столбец ребра с заданным номером у всех вершин
    private void removeEdgeColumn(int edgeIndex) {
        for (List<Integer> row : incidence) {
            row.remove(edgeIndex);
        }
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