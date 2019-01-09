package com.wxscistor.config;

import org.junit.Test;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

import static org.junit.Assert.*;

public class VertexiumConfigTest {

    @Test
    public void createAccumuloGraphWithNoSeach() {
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraphWithNoSeach("dlvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
    }
}