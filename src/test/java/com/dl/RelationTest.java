package com.dl;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;

public class RelationTest {
    @Test
    public void listrelation(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        Vertex vertex = VertexiumConfig.defaultGraph.getVertex("EVENT_POSTEVENT_0519d6bd84d040adfec704020d02a850", auth);
        System.out.println(vertex);

        Iterable<String> vertices = vertex.getVertexIds(Direction.IN, auth);
        vertices.forEach(x->{
            System.out.println(x);
        });

        System.out.println("out=========");
        Iterable<String> vertices2 = vertex.getVertexIds(Direction.OUT, auth);
        vertices2.forEach(x->{
            System.out.println(x);
        });
//        GraphQuery query = VertexiumConfig.defaultGraph.query(auth);
//        query.has("com.scistor.property.peoplename","汤尉");
//        query.vertices().forEach(x->{
//            System.out.println(x);
//        });

    }

    @Test
    public void findPath(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        Vertex vertex = VertexiumConfig.defaultGraph.getVertex("ENTITY_POSTMAN_b087388322f94238b44cd484ee1bee48", auth);
        FindPathOptions findPathOptions = new FindPathOptions("ENTITY_POSTMAN_b087388322f94238b44cd484ee1bee48", "ENTITY_POSTMAN_a4a6ee1663105b03e49ea8336ceb6b3b", 100);
        Iterable<Path> paths = VertexiumConfig.defaultGraph.findPaths(findPathOptions, auth);
        for (Path x : paths) {
            System.out.println(x);
        }
    }

    @Test
    public void tmp1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph mergevertexium = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = AuthUtils.getRootAuth(mergevertexium);
        VertexBuilder vertex1 = mergevertexium.prepareVertex("EVENT_FACEBOOK_POST_c316b867e712c77fbe47466366d74fdd", Visibility.EMPTY);
//        Vertex vertex = mergevertexium.getVertex("EVENT_FACEBOOK_POST_c316b867e712c77fbe47466366d74fdd", auth);
//        vertex.setProperty("dltestpro",1, Visibility.EMPTY,auth);
        vertex1.save(auth);
        mergevertexium.flush();
        FindPathOptions findPathOptions = new FindPathOptions("ENTITY_POSTMAN_b087388322f94238b44cd484ee1bee48", "ENTITY_POSTMAN_a4a6ee1663105b03e49ea8336ceb6b3b", 100);
        Iterable<Path> paths = VertexiumConfig.defaultGraph.findPaths(findPathOptions, auth);
        for (Path x : paths) {
            System.out.println(x);
        }
    }
}
