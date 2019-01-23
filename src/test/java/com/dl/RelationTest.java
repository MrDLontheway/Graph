package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.Direction;
import org.vertexium.FindPathOptions;
import org.vertexium.Path;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
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
}
