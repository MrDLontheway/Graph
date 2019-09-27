package com.scistor.vertexium;

import com.dl.speed.SaveDataHandler;
import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.elasticsearch5.ElasticsearchSearchQueryBase;
import org.vertexium.elasticsearch5.IndexRefreshTracker;
import org.vertexium.query.*;
import org.vertexium.util.CloseableIterable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class T {
    @Test
    public void t1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("sinanspeed");
                AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");

        AuthUtils.addRootAuth(graph,new String[]{"admin","vis1","vis22"});
//        graph.getEdges(auth).forEach(e->{
//            System.out.println(e);
//        });
//        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");
//        System.out.println(auth);
        VertexBuilder vertexBuilder = graph.prepareVertex("123", Visibility.EMPTY);
        if(vertexBuilder instanceof ElementBuilder){
            ElementBuilder e = vertexBuilder;
        }
//        vertexBuilder.save(auth);
//        graph.deleteVertex("123",auth);
        graph.flush();
//        long edgeCount = graph.getEdgeCount(auth);
//        long vertexCount = graph.getVertexCount(auth);
//        System.out.println(vertexCount);
//        System.out.println(edgeCount);

    }

    @Test
    public void t2() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("sinanspeed");
        AuthUtils.addRootAuth(graph,new String[]{"admin","vis1","vis22"});
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");

        //1568969405073   1568969405073  1568969405073  1568969405073 1568969405073
        Vertex v = graph.getVertex("ver123", auth);
        Vertex v2 = graph.getVertex("ver456", auth);

//        Edge save = graph.prepareEdge("e123", "ver123", "ver456", "label", Visibility.EMPTY).save(auth);
//        v.setProperty("idname","123",Visibility.EMPTY,auth);
//        graph.flush();

        System.out.println(v.getTimestamp());
        System.out.println(v2.getTimestamp());

//        VertexBuilder ver123 = graph.prepareVertex("ver123", Visibility.EMPTY);
//        ver123.setProperty("id","23",Visibility.EMPTY);
//        Vertex save = ver123.save(auth);
//        System.out.println(save.getTimestamp());
        //.setProperty("id","123",Visibility.EMPTY).save(auth);
        graph.flush();
//        QueryResultsIterable<Vertex> vertices = graph.query(auth)
//                .skip(0)
//                .limit(50)
//                .has("com-scistor-property-share_location","长汀街124号-9-7")
//                .vertices();
//        long totalHits = vertices.getTotalHits();

//        Iterable<Vertex> vs = graph.getVertices(0L,0L,auth);
//
//        vs.forEach(v->{
//            System.out.println(v);
//        });

        System.out.println(1);
//        Iterable<Vertex> vs2 = graph.getVertices(auth);
//        vertices.forEach(v->{
//            Vertex v1 = null;
//            v1.getTimestamp();
//            Property p = v.getProperty("123");
//            p.getTimestamp();
//            System.out.println(v);
//        });
//        System.out.println(new Date().toLocaleString());
//        long edgeCount = graph.getEdgeCount(auth);
//        long vertexCount = graph.getVertexCount(auth);
//        System.out.println(vertexCount);
//        System.out.println(edgeCount);

    }

    @Test
    public void esbulk(){
//        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("diy1");
//        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");

//        System.out.println(IndexRefreshTracker.class.getProtectionDomain().getCodeSource().getLocation());
//        VertexBuilder vertexBuilder = graph.prepareVertex("123", Visibility.EMPTY);
//        vertexBuilder.save(auth);
//        graph.flush();
//
//        graph.deleteVertex("123",auth);
//        graph.flush();

        Date s = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String format = simpleDateFormat.format(s);
        System.out.println(format);

    }

    @Test
    public void tt1() throws InterruptedException, AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("rollover");

        AccumuloAuthorizations dsrAuth = AuthUtils.getRootAuth(graph);
        org.elasticsearch.node.Node n = null;
        Query query = graph.query(dsrAuth).skip(0).limit(10);
        query.vertices().forEach(vertex -> {
            vertex.setProperty("dlage",10,Visibility.EMPTY,dsrAuth);
            System.out.println(vertex);
        });
        graph.flush();
        query.edges().forEach(edge -> {
            System.out.println(edge);
        });
//        long totalHits = query.edgeIds().getTotalHits();
//        TermsAggregation agg = new TermsAggregation("terms-count", Elasticsearch5SearchIndex.ELEMENT_TYPE_FIELD_NAME);
//        query.addAggregation(agg);
//
//        TermsResult aggregationResult = query.edgeIds().getAggregationResult("terms-count", TermsResult.class);
//        Iterable<TermsBucket> buckets = aggregationResult.getBuckets();
//        buckets.forEach(res->{
//            System.out.println(res);
//        });
    }

    @Test
    public void async(){
        System.out.println(123);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        System.out.println("finshed");
    }
}
