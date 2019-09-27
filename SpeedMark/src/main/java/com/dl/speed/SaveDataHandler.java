package com.dl.speed;

import com.wxscistor.concurrent.MGraphDBManager;
import org.vertexium.Element;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveDataHandler implements Runnable {
    private Iterable<org.vertexium.ElementBuilder<Element>> data = null;
    private AccumuloGraph graph = null;
    private AccumuloAuthorizations auth = null;

    public SaveDataHandler(AccumuloGraph graph, AccumuloAuthorizations auth, Iterable<org.vertexium.ElementBuilder<Element>> data){
        this.graph = graph;
        this.data = data;
        this.auth = auth;
    }

    @Override
    public void run() {
        List<Element> elements = new ArrayList<>();
        data.forEach(elementElementBuilder -> {
            Element save = elementElementBuilder.save(auth);
            elements.add(save);
        });
        graph.getSearchIndex().addElements(graph,elements,auth);
//        graph.flushGraph();
//        MGraphDBManager.close(graph);
//        graph.getSearchIndex().addElements(graph,elements,auth);
    }
}
