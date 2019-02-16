package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;
import org.vertexium.query.Query;
import org.vertexium.query.QueryResultsIterable;
import org.vertexium.query.SortDirection;

import java.util.*;

public class AccumuloGraphSort {
    @Test
    public void sort1(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("dltestvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
//        VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
//        vertexBuilder.setProperty("com.dl.property.name","代乐",Visibility.EMPTY);
//        vertexBuilder.setProperty("com.dl.property.name","代乐222",new Visibility("fjjtest"));
//        vertexBuilder.setProperty("com.scistor.property.age2",33,new Visibility("fjjtest"));
//        vertexBuilder.setProperty("com.scistor.property.age2",11,Visibility.EMPTY);
//        vertexBuilder.save(auth);

//        Vertex vertex = defaultGraph.getVertex("eb00ae1e496c4306a6de7698473a1934", auth);
//        Iterable<Property> properties = vertex.getProperties();
//        Iterable<Property> fjjtest = vertex.getProperties(new Visibility("123"));
//        System.out.println(vertex);
        Date d = new Date();
        GraphQuery query = defaultGraph.query(auth);
        Query sort = query.sort("com-scistor-property-age2", SortDirection.DESCENDING);
        sort.skip(0);
        sort.limit(20);
//        QueryResultsIterable<String> strings = sort.vertexIds();
//        strings.forEach(x->{
//            System.out.println(x);
//        });

        sort.vertices().forEach(x->{
            System.out.println(x.getId());
            Property property = x.getProperty("com-scistor-property-age2");
            if(property==null){
                System.out.println("null====");
            }else {
                System.out.println(property.getName()+"====="+property.getValue()+"===="+property.getVisibility());
            }
        });

        System.out.println(new Date().getTime()-d.getTime()+"ms=======");
//        List<Vertex> verticesInOrder = defaultGraph.getVerticesInOrder(strings, auth);

//        for (Vertex x : verticesInOrder) {
//            System.out.println(x.getId());
//            Property property = x.getProperty("com-scistor-property-age2");
//            if(property==null){
//                System.out.println("null====");
//            }else {
//                System.out.println(property.getName()+"====="+property.getValue());
//            }
//        }
        defaultGraph.flush();
        Collection<PropertyDefinition> propertyDefinitions = defaultGraph.getPropertyDefinitions();

//        HashSet<TextIndexHint> textIndexHints = new HashSet<>();
//        textIndexHints.add(TextIndexHint.EXACT_MATCH);
//        textIndexHints.add(TextIndexHint.FULL_TEXT);
//        defaultGraph.savePropertyDefinition(new PropertyDefinition("com-scistor-property-age2",Integer.class,textIndexHints,null,true));
        System.out.println(propertyDefinitions);
    }

    @Test
    public void tmp1(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("dltestvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

        HashSet<TextIndexHint> textIndexHints = new HashSet<>();
        textIndexHints.add(TextIndexHint.EXACT_MATCH);
        textIndexHints.add(TextIndexHint.FULL_TEXT);
        defaultGraph.savePropertyDefinition(new PropertyDefinition("com-scistor-property-age2",Integer.class,textIndexHints,null,true));
        defaultGraph.flush();

        Random r=  new Random();
        ArrayList<ElementBuilder<Vertex>> eb = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
            if(r.nextBoolean()){
                vertexBuilder.setProperty("com.scistor.property.age2",r.nextInt(100),new Visibility("vis1"));
                vertexBuilder.setProperty("com.scistor.property.age2",r.nextInt(100),new Visibility("fjjtest"));
            }else {
                vertexBuilder.setProperty("com.scistor.property.age2",r.nextInt(100),new Visibility("vis1"));
                vertexBuilder.setProperty("com.scistor.property.age2",r.nextInt(100),new Visibility("fjjtest"));
            }
            eb.add(vertexBuilder);
//            vertexBuilder.save(auth);
        }

        defaultGraph.addVerticesAsync(eb,auth);
        defaultGraph.flush();
    }

    @Test
    public void pro(){
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;//VertexiumConfig.createAccumuloGraph("dltestvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22");

        defaultGraph.getPropertyDefinitions().forEach(x->{
            PropertyDefinition propertyDefinition = new PropertyDefinition(x.getPropertyName(), x.getDataType(), x.getTextIndexHints(), null, true);
            defaultGraph.savePropertyDefinition(propertyDefinition);
        });
        defaultGraph.flush();
//        HashSet<TextIndexHint> textIndexHints = new HashSet<>();
//        textIndexHints.add(TextIndexHint.EXACT_MATCH);
//        textIndexHints.add(TextIndexHint.FULL_TEXT);
////        defaultGraph.savePropertyDefinition(new PropertyDefinition("com-scistor-property-age2",Integer.class,textIndexHints,null,true));
//        defaultGraph.flush();
    }
}
