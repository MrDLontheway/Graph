package com.wxscistor.config;

import org.junit.Test;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;
import org.vertexium.query.QueryResultsIterable;

public class VertexiumConfigTest {

    @Test
    public void createAccumuloGraphWithNoSeach() {
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraphWithNoSeach("wikivertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations();

        GraphQuery query = defaultGraph.query(auth);
        query.has("Objecturi","com.scistor.object.entity.link");
//        query.limit(100);
//        query.skip(10);
        QueryResultsIterable<Vertex> vertices = query.vertices();
        vertices.forEach(x->{
            System.out.println(123);
        });
    }
}