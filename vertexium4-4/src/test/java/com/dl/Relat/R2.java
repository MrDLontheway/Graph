package com.dl.Relat;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.QueryResultsIterable;

public class R2 extends Relat{

    @Test
    public void test1(){
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("nongraphindex");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");

        QueryResultsIterable<Vertex> vertices = graph.query("*昌平*", auth).vertices();
        long totalHits = vertices.getTotalHits();
        vertices.forEach(v->{
            System.out.println(v);
        });
    }
}
