package ru.nsu.berdyugin;

class AdjacencyMatrixGraphTest extends GraphContractTest {

    @Override
    protected Graph createGraph() {
        return new AdjacencyMatrixGraph();
    }
}