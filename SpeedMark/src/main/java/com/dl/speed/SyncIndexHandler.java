package com.dl.speed;

import com.wxscistor.concurrent.MGraphDBManager;
import org.vertexium.Element;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

public class SyncIndexHandler implements Runnable{
    private Iterable<Element> data = null;
    private AccumuloGraph graph = null;
    private AccumuloAuthorizations auth = null;

    public SyncIndexHandler(AccumuloGraph graph, AccumuloAuthorizations auth, Iterable<Element> data){
        this.graph = graph;
        this.data = data;
        this.auth = auth;
    }

    @Override
    public void run() {
        graph.getSearchIndex().addElements(graph,data,auth);
//        MGraphDBManager.close(graph);
    }
}
