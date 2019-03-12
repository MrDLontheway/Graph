package com.dl.Relat;

import com.com.wxscistor.Child;
import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloElement;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.accumulo.AccumuloVertex;
import org.vertexium.query.*;
import org.vertexium.type.GeoPoint;

import java.io.*;
import java.sql.Connection;
import java.time.Year;
import java.util.*;

public class Relat implements Serializable {
    public transient AccumuloGraph connection= VertexiumConfig.defaultGraph;
    public String name = "123";
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
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22","admin");

//        Edge edge = defaultGraph.getEdge("EVENT_FACEBOOK_POST_8c9b3816e23e8581d6abc092924935aa", auth);
//        edge.setProperty("com.dl.tmp.pros.t1","123456",new Visibility("knowledgebase_kafka"),auth);
//        defaultGraph.flush();
        PropertyDefinition pdf = defaultGraph.getPropertyDefinition("com.scistor.property.tags");
//        defaultGraph.savePropertyDefinition(new PropertyDefinition(pdf.getPropertyName(),pdf.getDataType(), EnumSet.of(TextIndexHint.FULL_TEXT),pdf.getBoost(),true));
//        defaultGraph.flush();
        HistogramAggregation aggregation = new HistogramAggregation("groupbydate", "TimeStart", "1M", 0L);
        Date date1 = new Date(0, 1, 1);
        Date date = new Date();
        HistogramAggregation.ExtendedBounds<Date> dateExtendedBounds = new HistogramAggregation.ExtendedBounds<Date>(date1,date);
        HistogramAggregation.ExtendedBounds<String> dateExtendedBounds2 = new HistogramAggregation.ExtendedBounds<String>("1990-01","1990-12");

        aggregation.setExtendedBounds(dateExtendedBounds2);
        Query query = defaultGraph.query(auth)
                .addAggregation(aggregation)
                .has("TimeStart");
        HistogramResult groupbydate = query.edges().getAggregationResult("groupbydate", HistogramResult.class);
//        Query objecturi = defaultGraph.query(auth)
//                .has("Objecturi", "com.scistor.object.event.twitter_post");
        query.vertices().forEach(x->{
            Property property = x.getProperty("com.scistor.property.time_interval.start");
            Object value = property.getValue();
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

    @Test
    public void tmp2(){
        Properties properties = System.getProperties();
        String java = properties.getProperty("java.home");
        String env = properties.getProperty("env.JAVA_HOME");

        System.out.println(java);
        System.out.println(env);
    }

    @Test
    public void tmp3() throws IOException, ClassNotFoundException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22","admin");

        AccumuloVertex next = (AccumuloVertex) defaultGraph.getVertices(auth).iterator().next();
        String path1 = AccumuloElement.class.getResource("/").getPath();
        System.out.println(path1);

        Child r = new Child(null,null,null);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("vertex"));
        outputStream.writeObject(next);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream vertex = new ObjectInputStream(new FileInputStream(
                new File("vertex")));
        AccumuloVertex o = (AccumuloVertex) vertex.readObject();

        Date date = new Date(189273600000L);
        String s = date.toLocaleString();
        System.out.println(date);
    }
}
