package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.ElementBuilder;
import org.vertexium.Vertex;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.search.IndexHint;

import java.util.ArrayList;

public class GraphBatch {

    @Test
    public void asyc() throws AccumuloSecurityException, AccumuloException {
        //1w 132s
//        for (int i = 0; i < 10000; i++) {
//            String root = AccumuloConnector.addAuths("root", "fjjtest,111111");
//        }
        AccumuloGraph properties = VertexiumConfig.createAccumuloGraph("dlvertexium");
        ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
        for (int i = 0; i < 12000; i++) {
            VertexBuilder vertexBuilder = properties.prepareVertex(Visibility.EMPTY);
            vertexBuilder.setProperty("com.dl.property.age",11,Visibility.EMPTY);
            vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
            vs.add(vertexBuilder);
        }
        properties.getSearchIndex().addElements(properties,properties.addVertices(vs,new AccumuloAuthorizations()),new AccumuloAuthorizations());
        properties.flush();
    }

    @Test
    public void sync() throws AccumuloSecurityException, AccumuloException {
        //1w 132s
//        for (int i = 0; i < 10000; i++) {
//            String root = AccumuloConnector.addAuths("root", "fjjtest,111111");
//        }
        AccumuloGraph properties = VertexiumConfig.createAccumuloGraph("dlvertexium");
        ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
        for (int i = 0; i < 12000; i++) {
            VertexBuilder vertexBuilder = properties.prepareVertex(Visibility.EMPTY);
            vertexBuilder.setProperty("com.dl.property.age",11,Visibility.EMPTY);
            vs.add(vertexBuilder);
        }
        properties.addVertices(vs,new AccumuloAuthorizations());
        properties.flush();
    }
}
