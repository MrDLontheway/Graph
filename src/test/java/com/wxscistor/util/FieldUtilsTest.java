package com.wxscistor.util;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.PropertyDefinition;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;

import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void getEsFieldNames() {
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest");
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        GraphQuery query = VertexiumConfig.defaultGraph.query(auth);
        System.out.println(PropertyDefinition.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String[] esFieldNames = FieldUtils.getEsFieldNames(graph, "com.scistor.property.jijiandizhi");
        System.out.println(esFieldNames);
    }

    @Test
    public void tolong(){

    }
}