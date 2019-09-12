package com.com.wxscistor;

import com.wxscistor.config.VertexiumConfig;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.PropertyDefinition;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.*;
import org.vertexium.type.GeoCircle;
import org.vertexium.type.GeoPoint;

import java.util.Collection;
import java.util.Random;

public class Geo {
    @Test
    public void geo(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations();
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder("123");
//        GeoPolygonQueryBuilder geoPolygonQueryBuilder = new GeoPolygonQueryBuilder(String fieldName, List<GeoPoint> points);
        GraphQuery query = defaultGraph.query(auth);
        query.has("com.scistor.dl.geopro1",GeoCompare.WITHIN,new GeoCircle(33.93612815356125, 62.54302156694006,5000));
//        query.geoDistance(new GeoDistance("com.scistor.dl.geopro1",
//                new GeoPoint(33.93612815356125, 62.54302156694006),
//                5.0,
//                DistanceUnit.KILOMETERS));

        QueryResultsIterable<String> strings = query.vertexIds();
        strings.forEach(System.out::println);
    }

    @Test
    public void insertData(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations();
        //41.9022770410,97.1630859375
        Random r= new Random();
        for (int i = 0; i < 100; i++) {
            GeoPoint geoPoint = new GeoPoint(r.nextDouble() * 50, r.nextDouble() * 100);
            VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
            defaultGraph.prepareVertex(Visibility.EMPTY)
                    .setProperty("com.scistor.dl.geopro1",geoPoint,Visibility.EMPTY)
                    .save(auth);
        }
        defaultGraph.flush();
    }

    @Test
    public void tmp(){
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations accumuloAuthorizations = new AccumuloAuthorizations();
        //初始化图库需要手动加入标签属性
        org.vertexium.PropertyDefinition propertyDefinition = new org.vertexium.PropertyDefinition("tags", new String[]{}.getClass(), TextIndexHint.NONE);
        graph.savePropertyDefinition(propertyDefinition);

//        System.exit(1);
        Vertex vertex = graph.getVertex("557524171412733952", accumuloAuthorizations);
//        VertexBuilder vertexBuilder = graph.prepareVertex("557524171412733952", Visibility.EMPTY);
//        String[] tags = new String[]{"保安","大叔"};
//        vertexBuilder.setProperty("tags",tags,Visibility.EMPTY);
//        vertexBuilder.save(accumuloAuthorizations);
//        graph.flush();
        System.out.println(vertex);
    }

    @Test
    public void tmp2(){
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations accumuloAuthorizations = new AccumuloAuthorizations();
        Collection<PropertyDefinition> propertyDefinitions = graph.getPropertyDefinitions();
        graph.flush();
    }
}
