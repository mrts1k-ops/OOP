package ru.nsu.berdyugin;

class IncidenceMatrixGraphTest extends GraphContractTest {

    @Override
    protected Graph createGraph() {
        return new IncidenceMatrixGraph();
    }
}