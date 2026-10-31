package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;



/**
 * Проверки, которые должны выполняться для ЛЮБОЙ реализации Graph.
 * (AdjacencyMatrixGraph, IncidenceMatrixGraph, AdjacencyListGraph).
 */

abstract class GraphContractTest {
    protected abstract Graph createGraph();

    @Test
    void newGraphHasNoVertices() {
        Graph graph = createGraph();
        assertTrue(graph.getVertices().isEmpty());
    }

    @Test
    void addedVerticesAppearInList() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);

        List<Integer> vertices = graph.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(0));
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));
    }

    @Test
    void addingSameVertexTwiceThrows() {
        Graph graph = createGraph();
        graph.addVertex(0);
        assertThrows(IllegalArgumentException.class, () -> graph.addVertex(0));
    }

    @Test
    void removingMissingVertexThrows() {
        Graph graph = createGraph();
        assertThrows(IllegalArgumentException.class, () -> graph.removeVertex(5));
    }

    @Test
    void addEdgeCreatesNeighbor() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        assertEquals(Arrays.asList(1), graph.getNeighbors(0));
        assertTrue(graph.getNeighbors(1).isEmpty());
    }

    @Test
    void addingSameEdgeTwiceDoesNotDuplicateIt() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.addEdge(0, 1);

        assertEquals(Arrays.asList(1), graph.getNeighbors(0));
    }

    @Test
    void removeEdgeRemovesNeighbor() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);

        assertTrue(graph.getNeighbors(0).isEmpty());
    }

    @Test
    void edgeBetweenMissingVerticesThrows() {
        Graph graph = createGraph();
        graph.addVertex(0);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 99));
    }

    @Test
    void getNeighborsAreSortedAscending() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 3);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);

        assertEquals(Arrays.asList(1, 2, 3), graph.getNeighbors(0));
    }

    @Test
    void removingVertexRemovesItsOwnEdges() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        graph.removeVertex(1);

        assertFalse(graph.getVertices().contains(1));
        assertTrue(graph.getNeighbors(0).isEmpty());
    }

    @Test
    void removingVertexRemovesEdgesLeadingToIt() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);

        graph.removeVertex(2);

        assertTrue(graph.getNeighbors(0).isEmpty());
        assertTrue(graph.getNeighbors(1).isEmpty());
    }

    @Test
    void equalsIsTrueForSameStructureRegardlessOfInsertionOrder() {
        Graph first = createGraph();
        first.addVertex(0);
        first.addVertex(1);
        first.addEdge(0, 1);

        Graph second = createGraph();
        second.addVertex(1);
        second.addVertex(0);
        second.addEdge(0, 1);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalsIsFalseForDifferentEdges() {
        Graph first = createGraph();
        first.addVertex(0);
        first.addVertex(1);
        first.addEdge(0, 1);

        Graph second = createGraph();
        second.addVertex(0);
        second.addVertex(1);

        assertFalse(first.equals(second));
    }

    @Test
    void toStringContainsVerticesAndEdges() {
        Graph graph = createGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        String text = graph.toString();
        assertTrue(text.contains("0"));
        assertTrue(text.contains("1"));
        assertTrue(text.contains("(0,1)"));
    }
}