package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;



class TopologicalSortTest {

    @Test
    void sortsSimpleChain() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertEquals(Arrays.asList(0, 1, 2), TopologicalSort.sort(graph));
    }

    @Test
    void verticesWithoutEdgesComeFirstByNumber() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(2);
        graph.addVertex(0);
        graph.addVertex(1);
        // Рёбер нет, значит порядок определяется только номером вершины

        assertEquals(Arrays.asList(0, 1, 2), TopologicalSort.sort(graph));
    }

    @Test
    void resultRespectsAllEdges() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> order = TopologicalSort.sort(graph);
        
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(2));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }

    @Test
    void cycleThrowsException() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);
        graph.addEdge(1, 0);

        assertThrows(IllegalStateException.class, () -> TopologicalSort.sort(graph));
    }

    @Test
    void worksTheSameForAnyRepresentation() {
        Graph matrix = new AdjacencyMatrixGraph();
        Graph incidence = new IncidenceMatrixGraph();
        for (Graph graph : new Graph[]{matrix, incidence}) {
            graph.addVertex(0);
            graph.addVertex(1);
            graph.addVertex(2);
            graph.addEdge(0, 1);
            graph.addEdge(1, 2);
        }

        assertEquals(TopologicalSort.sort(matrix), TopologicalSort.sort(incidence));
    }
}