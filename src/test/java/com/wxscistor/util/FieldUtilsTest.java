package com.wxscistor.util;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class FieldUtilsTest {
    @Test
    public void getEsFieldNames() {
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22");
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        PropertyDefinition propertyDefinition = graph.getPropertyDefinition("com-scistor-property-time_interval-timeStart");
        System.exit(1);
        GraphQuery query = VertexiumConfig.defaultGraph.query(auth);
        Collection<PropertyDefinition> propertyDefinitions = VertexiumConfig.defaultGraph.getPropertyDefinitions();
        System.out.println(PropertyDefinition.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String[] esFieldNames = FieldUtils.getEsFieldNames(graph, "com.scistor.property.reply_user_screen_name",new String[]{"knowledgebase_kafka","fjjtest","vis1","vis22"});
        System.out.println(esFieldNames);
    }

    @Test
    public void manvis(){
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22");
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("vertexium");

        Vertex dltest = graph.getVertex("dltest", auth);
        Iterable<Property> properties = dltest.getProperties(new Visibility("123"));
        properties.forEach(x->{
            System.out.println(x);
        });

//        VertexBuilder dltest = graph.prepareVertex("dltest", Visibility.EMPTY);
//        dltest.setProperty("com.dl.provis1","1234",new Visibility("fjjtest"));
//        dltest.setProperty("com.dl.provis1","234567",new Visibility("knowledgebase_kafka"));
//        dltest.save(auth);
//        graph.flush();

    }
}