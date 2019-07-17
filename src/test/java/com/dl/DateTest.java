package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.vertexium.PropertyDefinition;
import org.vertexium.TextIndexHint;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

import java.util.Date;
import java.util.EnumSet;

import static org.vertexium.TextIndexHint.FULL_TEXT;

public class DateTest {
    public static void main(String[] args) {
        AccumuloGraph graph = VertexiumConfig.createAccumuloGraph("tmpver");
        //初始化图库需要手动加入标签属性
//        PropertyDefinition propertyDefinition = new PropertyDefinition(CompassUtils.ES_TAG, new String[]{}.getClass(), EnumSet.of(FULL_TEXT));
        PropertyDefinition propertyDefinition2 = new PropertyDefinition("com-scistor-property-time_intervaltimeStart", Date.class, TextIndexHint.ALL);
        PropertyDefinition propertyDefinition3 = new PropertyDefinition("com-scistor-property-time_intervalt_imeStart", Date.class, TextIndexHint.ALL);

//        graph.savePropertyDefinition(propertyDefinition);
        graph.savePropertyDefinition(propertyDefinition2);
        graph.savePropertyDefinition(propertyDefinition3);
        graph.flush();

        VertexBuilder vertexBuilder = graph.prepareVertex(Visibility.EMPTY);
//        vertexBuilder.setProperty("com.scistor.property.time_intervaltimeStart","2017-08-28 11:37:20",Visibility.EMPTY);
//        vertexBuilder.setProperty("com.scistor.property.time_intervaltimeStart",new Date().toLocaleString(),Visibility.EMPTY);

        vertexBuilder.save(new AccumuloAuthorizations());
        graph.flush();
    }
}
