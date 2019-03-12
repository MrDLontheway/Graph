package com.dl;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.Edge;
import org.vertexium.Vertex;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

public class RandomLongId {
    @Test
    public void testinsert() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph vertexium = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = AuthUtils.getRootAuth(vertexium);

        Vertex test123 = vertexium.prepareVertex("test123", Visibility.EMPTY).save(auth);
        Vertex test123456 = vertexium.prepareVertex("test123456", Visibility.EMPTY).save(auth);
        Edge save = vertexium.prepareEdge("edge123", "test123456", "test123", Visibility.EMPTY).save(auth);
        Edge save2 = vertexium.prepareEdge("edge123", "hahhahahh", "test123", Visibility.EMPTY).save(auth);
        vertexium.flush();
    }
}
