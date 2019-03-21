package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.elasticsearch5.ElasticsearchSearchQueryBase;
import org.vertexium.query.GraphQuery;
import org.vertexium.search.SearchIndex;

import java.util.Collection;
import java.util.Iterator;

public class QuerTest {

    @Test
    public void termKeyWord(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest");
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        GraphQuery query = VertexiumConfig.defaultGraph.query("",auth);
        SearchIndex searchIndex = graph.getSearchIndex();
        query.vertices().forEach(System.out::println);
    }

    @Test
    public void syncTest(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","111111");
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        Vertex vertex = graph.getVertex("ENTITY_POSTMAN_adf6ada83fdf826a9afefa826918e950", auth);
        vertex.setProperty("com.scistor.property.peoplename","夏夏",new Visibility("111111"),auth);
        graph.flush();
    }

    @Test
    public void listReslication(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","111111");
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        GraphQuery query = VertexiumConfig.defaultGraph.query("期昌路376号",auth);
        Vertex vertex = graph.getVertex("ENTITY_POSTMAN_adf6ada83fdf826a9afefa826918e950", auth);
        Object propertyValue = vertex.getPropertyValue(null,"com.scistor.property.peoplename".replace(".","-"),"fjjtest");
        Iterable<Vertex> vertexIds = vertex.getVertices(Direction.OUT, auth);

        Iterator<Vertex> iterator = vertexIds.iterator();
        while (iterator.hasNext()){
            Vertex x = iterator.next();
            if("com.scistor.object.entity".equals(x.getPropertyValue("Objecttype"))){
                EdgesSummary edgesSummary = x.getEdgesSummary(auth);
                System.out.println(edgesSummary);
            }
        }
    }
}
