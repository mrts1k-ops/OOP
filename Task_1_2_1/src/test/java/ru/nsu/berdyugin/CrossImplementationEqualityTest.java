package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;


/**
 * Equals() должен считать графы равными, даже если это разные классы.
 */

class CrossImplementationEqualityTest {

    @Test
    void matrixEqualsIncidence() {
        Graph matrix = new AdjacencyMatrixGraph();
        Graph incidence = new IncidenceMatrixGraph();
        fillSameGraph(matrix);
        fillSameGraph(incidence);

        assertEquals(matrix, incidence);
    }

    @Test
    void matrixEqualsList() {
        Graph matrix = new AdjacencyMatrixGraph();
        Graph list = new AdjacencyListGraph();
        fillSameGraph(matrix);
        fillSameGraph(list);

        assertEquals(matrix, list);
    }

    @Test
    void differentGraphsAreNotEqualAcrossImplementations() {
        Graph matrix = new AdjacencyMatrixGraph();
        matrix.addVertex(0);
        matrix.addVertex(1);
        matrix.addEdge(0, 1);

        Graph list = new AdjacencyListGraph();
        list.addVertex(0);
        list.addVertex(1); // Ребра нет значит графы разные

        assertFalse(matrix.equals(list));
    }

    // Заполняет граф одними и теми же вершинами и рёбрами
    private void fillSameGraph(Graph graph) {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
    }
}