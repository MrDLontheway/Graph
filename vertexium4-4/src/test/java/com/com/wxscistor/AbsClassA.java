package com.com.wxscistor;

import com.dl.DiyGraph;
import com.dl.QuerTest;
import com.wxscistor.config.VertexiumConfig;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.Compare;
import org.vertexium.query.GraphQuery;

import java.io.Serializable;

public abstract class AbsClassA implements Serializable {
    public String name = "123";
    public transient Graph graph = VertexiumConfig.defaultGraph;
    private transient DiyGraph diyGraph = new DiyGraph();
    public final transient QuerTest querTest = new QuerTest();
    AbsClassA(Graph graph,DiyGraph diyGraph,QuerTest querTest){
    }

    public static void main(String[] args) throws InterruptedException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations();
        Iterable<Vertex> vertices = defaultGraph.getVertices(auth);
        Iterable<Edge> edges = defaultGraph.getEdges(auth);
        for (Edge edge : edges) {
        }
        vertices.forEach(x->{
            Iterable<String> vertexIds = x.getVertexIds(Direction.BOTH, auth);
            System.out.println(x);
        });

        VertexBuilder vis = defaultGraph.prepareVertex("123",new Visibility("vis,vis2"));
        vis.setProperty("name","zhangsan",Visibility.EMPTY);
        vis.save(auth);

        Vertex vertex = defaultGraph.getVertex("123", auth);

        GraphQuery query = defaultGraph.query(auth);
        query.has("name","lisi")
                .has("age", Compare.LESS_THAN_EQUAL,10)
                .hasId("123","1234");

        
//        Random random = new Random();
//        while (true){
//            int i = random.nextInt(10000);
//            System.out.println("echo ================"+i);
//            Thread.sleep(1000);
//        }
    }
}
