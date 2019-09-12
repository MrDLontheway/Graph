package com.scistor.vertexium;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

public class T {
    @Test
    public void t1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("none5000bulk");
        AuthUtils.addRootAuth(graph,new String[]{"admin","vis1","vis22"});
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");
        System.out.println(auth);
        VertexBuilder vertexBuilder = graph.prepareVertex("123", Visibility.EMPTY);
        vertexBuilder.save(auth);
        graph.deleteVertex("123",auth);
        graph.flush();
//        long edgeCount = graph.getEdgeCount(auth);
//        long vertexCount = graph.getVertexCount(auth);
//        System.out.println(vertexCount);
//        System.out.println(edgeCount);

    }

    @Test
    public void t2() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraphWithNoSeach("test");
        AuthUtils.addRootAuth(graph,new String[]{"admin","vis1","vis22"});
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");

//        long edgeCount = graph.getEdgeCount(auth);
//        long vertexCount = graph.getVertexCount(auth);
//        System.out.println(vertexCount);
//        System.out.println(edgeCount);

    }

    @Test
    public void esbulk(){
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("bulktest");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("admin","vis1","vis22");
        VertexBuilder vertexBuilder = graph.prepareVertex("123", Visibility.EMPTY);
        vertexBuilder.save(auth);
        graph.flush();

        graph.deleteVertex("123",auth);
        graph.flush();


    }
}
