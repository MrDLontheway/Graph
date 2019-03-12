package com.dl;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.search.IndexHint;
import org.vertexium.search.SearchIndex;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AccumuloGraphTest {

    @Test
    public void sycnTest(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("batchvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
        vertexBuilder.setProperty("com.dl.property.name","代乐",Visibility.EMPTY);
        vertexBuilder.save(auth);
        Random r = new Random();
        int number = 2000000;
        for (int i = 0; i < number; i++) {
            VertexBuilder vertexBuilder2 = defaultGraph.prepareVertex(Visibility.EMPTY);
            vertexBuilder2.setIndexHint(IndexHint.DO_NOT_INDEX);
            vertexBuilder2.setProperty("com.dl.property.name","代乐",Visibility.EMPTY);
            vertexBuilder2.setProperty("com.dl.property.age",r.nextInt(100),Visibility.EMPTY);
            vertexBuilder2.save(auth);
        }
        defaultGraph.flush();
//        defaultGraph.getVertices(auth).forEach(x->{
//            System.out.println(x);
//        });
    }

    @Test
    public void dropGraph(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("dlvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest");

        defaultGraph.drop();
//        defaultGraph.flush();
    }


    @Test
    public void concurrent() throws InterruptedException {
        //
        //30 线程  300w 数据  插入点 129s  单对象 2m8s912ms  2.5w/s
        //30 线程  300w 数据  插入点 129s  多线程异步 1m51s273ms  112s  2.6785w/s
        //50 线程  1000w 数据  500w点 500w边  多线程异步  12m4s18ms 724s   1.4w/s
        ExecutorService executor = new ThreadPoolExecutor(30, 50, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 50; i++) {
            executor.submit(new ConsumerThreadHandler());
        }

        while (true){
            if(((ThreadPoolExecutor) executor).getCompletedTaskCount()==30){
                break;
            }else {
                Thread.sleep(1000);
            }
        }
    }

    @Test
    public void tmp(){
        Graph graph= VertexiumConfig.defaultGraph;
        final AccumuloAuthorizations auth = new AccumuloAuthorizations();

        VertexBuilder vertexBuilder = graph.prepareVertex(Visibility.EMPTY);
        vertexBuilder.setProperty("name","test",Visibility.EMPTY);
        vertexBuilder.setProperty("age",10,Visibility.EMPTY);
        vertexBuilder.save(auth);
        graph.flush();
    }


    @Test
    public void writeToes(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("dlvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

        AccumuloGraph vertexium = VertexiumConfig.createAccumuloGraph("vertexium");
        Iterable<Vertex> vertices = defaultGraph.getVertices(auth);

        vertexium.getSearchIndex().addElements(vertexium,vertices,auth);
        vertexium.flush();
    }

    @Test
    public void getpon(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

        SearchIndex searchIndex = defaultGraph.getSearchIndex();
        final AccumuloAuthorizations userAuth = auth;
        String[] fields = new String[]{};
        if(searchIndex instanceof Elasticsearch5SearchIndex){
            Set<String> allPropertyNames = defaultGraph.getAllPropertyNames();
            Collection<String> queryablePropertyNames = ((Elasticsearch5SearchIndex) searchIndex).getQueryablePropertyNames(defaultGraph, userAuth);
            fields = queryablePropertyNames.toArray(new String[0]);
        }

    }

    @Test
    public void emp(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22","111111");

        Vertex event_postevent_c145ab96e2ad06149cf79f981d930029 = defaultGraph.getVertex("EVENT_POSTEVENT_c145ab96e2ad06149cf79f981d930029", auth);
        Collection<PropertyDefinition> propertyDefinitions = defaultGraph.getPropertyDefinitions();
        propertyDefinitions.forEach(x->{
            System.out.println(x.getPropertyName()+"========"+x.isSortable());
        });

    }
}
