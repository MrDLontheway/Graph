package com.dl.Relat;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;
import org.vertexium.query.Query;
import org.vertexium.type.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relat {
    @Test
    public void r1(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
        List<String> strings = Arrays.asList(ids);
        Iterable<RelatedEdge> relatedEdgeSummary = defaultGraph.findRelatedEdgeSummary(strings, auth);
        relatedEdgeSummary.forEach(x->{
            Vertex vertex = defaultGraph.getVertex(x.getOutVertexId(), auth);
            Edge edge = defaultGraph.getEdge(x.getEdgeId(), auth);
            System.out.println(edge);
        });
        Iterable<String> relatedEdgeIds = defaultGraph.findRelatedEdgeIds(strings, auth);
        relatedEdgeIds.forEach(x->{
            System.out.println(x);
        });
    }

    @Test
    public void r2(){
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22");

//        Edge edge = defaultGraph.getEdge("EVENT_FACEBOOK_POST_8c9b3816e23e8581d6abc092924935aa", auth);
//        edge.setProperty("com.dl.tmp.pros.t1","123456",new Visibility("knowledgebase_kafka"),auth);
//        defaultGraph.flush();
        Query objecturi = defaultGraph.query(auth)
                .has("Objecturi", "com.scistor.object.event.twitter_post");
        objecturi.vertices().forEach(x->{
            System.out.println(x);
        });

//        VertexBuilder test1111 = defaultGraph.prepareVertex("test1111", Visibility.EMPTY);
//        org.elasticsearch.common.geo.GeoPoint
//        ElementBuilder<Vertex> des = test1111.setProperty("com.dl.pros", new GeoPoint(10.11, 12.33, "desaaa"), Visibility.EMPTY);
//        Vertex save = des.save(auth);
//        defaultGraph.flush();
//        GraphQuery query = defaultGraph.query("*150*", auth);
//        query.vertices().forEach(x->{
//            System.out.println(x);
//        });
    }
}
