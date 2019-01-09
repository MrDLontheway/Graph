package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.Graph;
import org.vertexium.Vertex;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.accumulo.LazyPropertyMetadata;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AccumuloGraphTest {

    public static void main(String[] args) {
        System.out.println(1);
    }
    @Test
    public void sycnTest(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("dlvertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");


        VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
        vertexBuilder.setProperty("com.dl.property.name","代乐",Visibility.EMPTY);
        vertexBuilder.save(auth);

        Random r = new Random();
        int number = 100;
        for (int i = 0; i < number; i++) {
            VertexBuilder vertexBuilder2 = defaultGraph.prepareVertex(Visibility.EMPTY);
            vertexBuilder2.setProperty("com.dl.property.name","代乐",Visibility.EMPTY);
            vertexBuilder2.setProperty("com.dl.property.age",r.nextInt(100),Visibility.EMPTY);
            vertexBuilder2.save(auth);
        }
        defaultGraph.flush();
        defaultGraph.getVertices(auth).forEach(x->{
            System.out.println(x);
        });
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
}
