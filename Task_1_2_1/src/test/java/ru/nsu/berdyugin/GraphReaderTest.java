package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;


class GraphReaderTest {

    @Test
    void readsVerticesAndEdges() {
        Graph graph = new AdjacencyListGraph();
        List<String> lines = Arrays.asList("4", "0 1", "0 2", "1 3", "2 3");

        GraphReader.readFromLines(graph, lines);

        assertEquals(4, graph.getVertices().size());
        assertEquals(Arrays.asList(1, 2), graph.getNeighbors(0));
        assertEquals(Arrays.asList(3), graph.getNeighbors(1));
    }

    @Test
    void readsGraphWithNoEdges() {
        Graph graph = new AdjacencyListGraph();
        List<String> lines = Arrays.asList("3");

        GraphReader.readFromLines(graph, lines);

        assertEquals(3, graph.getVertices().size());
        assertEquals(0, graph.getNeighbors(0).size());
    }

    @Test
    void ignoresEmptyLinesAtTheEnd() {
        Graph graph = new AdjacencyListGraph();
        List<String> lines = Arrays.asList("2", "0 1", "");

        GraphReader.readFromLines(graph, lines);

        assertEquals(Arrays.asList(1), graph.getNeighbors(0));
    }
}