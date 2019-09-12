package com.wxscistor.pojo.vertexium;

import org.vertexium.Edge;
import org.vertexium.Vertex;

public class GraphRelation{
    private Vertex out;
    private Edge edge;
    private Vertex in;

    public Vertex getOut() {
        return out;
    }

    public void setOut(Vertex out) {
        this.out = out;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Vertex getIn() {
        return in;
    }

    public void setIn(Vertex in) {
        this.in = in;
    }

    public GraphRelation(Vertex out, Edge edge, Vertex in) {
        this.out = out;
        this.edge = edge;
        this.in = in;
    }
}
