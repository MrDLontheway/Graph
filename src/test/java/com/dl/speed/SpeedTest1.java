package com.dl.speed;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.QueryResultsIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedTest1 {
//    @Test
//    public void one(){
//        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
//        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
//        for (int i = 0; i < 1000; i++) {
//            Vertex vertex = defaultGraph.getVertex("ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452", auth);
//        }
//    }
//
//    @Test
//    public void more(){
//        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
//        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
//        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
//                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
//                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
//        List<String> strings = Arrays.asList(ids);
//        for (int i = 0; i < 1000; i++) {
//            Iterable<Vertex> vertices = defaultGraph.getVertices(strings, auth);
//            vertices.forEach(x->{
//
//            });
//        }
//    }

    @Test
    public void getVertexs(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
        List<String> strings = Arrays.asList(ids);
        for (int i = 0; i < 1000; i++) {
            Iterable<Vertex> vertices = defaultGraph.getVertices(strings, auth);
            vertices.forEach(x->{
                Iterable<Vertex> vertices1 = x.getVertices(Direction.BOTH, auth);
                vertices1.forEach(y->{
                    Iterable<Vertex> vertices2 = y.getVertices(Direction.OUT, auth);
                    Iterable<String> vertexIds = y.getVertexIds(Direction.OUT, auth);
                    System.out.println(y);
                });
            });
        }
    }

    @Test
    public void getVertexsOnlyId(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
        List<String> strings = Arrays.asList(ids);
        for (int i = 0; i < 1000; i++) {
            Iterable<Vertex> vertices = defaultGraph.getVertices(strings, auth);
            vertices.forEach(x->{
                Iterable<String> vertices1 = x.getVertexIds(Direction.BOTH, auth);
                vertices1.forEach(y->{

                });
            });
        }
    }

    @Test
    public void myGetVertexs(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
        List<String> strings = Arrays.asList(ids);
        for (int i = 0; i < 1000; i++) {
            ArrayList<String> ids2 = new ArrayList<>();
            Iterable<Vertex> vertices = defaultGraph.getVertices(strings, auth);
            vertices.forEach(x->{
                Iterable<String> vertices1 = x.getVertexIds(Direction.BOTH, auth);
                vertices1.forEach(y->{
                    ids2.add(y);
                });
            });
            Iterable<Vertex> vertices1 = defaultGraph.getVertices(ids2, auth);
            vertices1.forEach(x->{

            });
        }
    }

    @Test
    public void tmp(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

        String[] strings = {"4c7fc3bb3f307d1ac9f2c490ba786d37",
                "223de7b05875580eca556e8b2bcbadf7",
                "0a0fe0781b8fe582277ce491174948f0"};
        QueryResultsIterable<Edge> edges = defaultGraph.query(auth).hasId(strings).edges();

        edges.forEach(x->{
            System.out.println(x);
        });
    }

    @Test
    public void tmp2(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

        Vertex vertex = defaultGraph.getVertex("ENTITY_POSTMAN_16ff79eb630960ff4caf93b000fd2811", auth);
        Property property = vertex.getProperty("com.dl.pro");
        String[] value = (String[]) property.getValue();
        Property objectLabel = vertex.getProperty("com.scistor.property.peoplename");
        System.out.println(123);
//        String[] strings = {"123", "heiheihei"};
//        int[] ints = {123, 456};
//        System.out.println(ints.getClass().getName());
////        System.exit(1);
//        vertex.setProperty("com.dl.pro",strings,Visibility.EMPTY,auth);
//        defaultGraph.flush();
    }
}
