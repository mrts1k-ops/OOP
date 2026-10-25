package ru.nsu.berdyugin;

class AdjacencyListGraphTest extends GraphContractTest {

    @Override
    protected Graph createGraph() {
        return new AdjacencyListGraph();
    }
}