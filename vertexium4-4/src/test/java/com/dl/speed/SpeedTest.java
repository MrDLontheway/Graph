package com.dl.speed;

import com.wxscistor.config.VertexiumConfig;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.ElementBuilder;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.GraphQuery;
import org.vertexium.query.QueryResultsIterable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SpeedTest {
    private List<String> objectUris = new ArrayList<String>();

    private Random r = new Random();

    @Test
    public void speedTest(){
        inint();
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("speedtestsimple");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        Visibility fjjtest = new Visibility("fjjtest");

        ArrayList<ElementBuilder<Vertex>> vertexBuilders = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String uri = getRandomeValue(objectUris);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            VertexBuilder vertexBuilder = defaultGraph.prepareVertex(uri + uuid, Visibility.EMPTY);
            int i1 = r.nextInt(100);
            int i2 = r.nextInt(100);
            vertexBuilder.setProperty("name",RandomValue.getChineseName(),fjjtest);
            vertexBuilder.setProperty("address",RandomValue.getRoad(),fjjtest);
            vertexBuilder.setProperty("home_address",RandomValue.getRoad(),fjjtest);
            vertexBuilder.setProperty("objecturi",uri,fjjtest);
            vertexBuilder.setProperty("number2",i1,fjjtest);
            vertexBuilder.setProperty("number3",i1,fjjtest);
            vertexBuilder.setProperty("age",i2,fjjtest);
            vertexBuilder.setProperty("age2",i2,fjjtest);
            vertexBuilder.setProperty("age3",i2,fjjtest);
            vertexBuilder.setProperty("start_time",new Date(),fjjtest);
            vertexBuilders.add(vertexBuilder);
        }

        defaultGraph.addVerticesAsync(vertexBuilders,auth);
        defaultGraph.flush();
//        long totalHits = defaultGraph.query(auth).vertices().getTotalHits();
//        assert (totalHits == 100000);
//        System.out.println(objectUris.size());
//        Vertex vertex = defaultGraph.getVertex("ENTITY_POSTMAN_16ff79eb630960ff4caf93b000fd2811", auth);
//        Property property = vertex.getProperty("com.dl.pro");
//        String[] value = (String[]) property.getValue();
//        Property objectLabel = vertex.getProperty("com.scistor.property.peoplename");
//        System.out.println(123);

    }

    @Test
    public void threadPool() throws InterruptedException {
        System.out.println(new Date().toLocaleString());
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(15);
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
//        fixedThreadPool.execute(new ReciveHandler("speedtestthread",null));
        Thread.sleep(1000000000);
    }

    public <T>T getRandomeValue(List<T> data){
        T datum = data.get(r.nextInt(data.size()));
        return datum;
    }

    public void inint(){
        objectUris.addAll(Arrays.asList(new String[]{"people","targetpeople","article","message","phoneNumber","phone"}));
    }

    @Test
    public void tt1(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("speedtest");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");

//        defaultGraph.getVertices(auth).forEach(vertex -> {
//            System.out.println(vertex);
//        });
        GraphQuery query = defaultGraph.query("*李*", auth);
        QueryResultsIterable<Vertex> result = query.vertices();
        AtomicInteger i = new AtomicInteger();
        result.forEach(vertex -> {
            System.out.println(vertex+"::::::"+ i.getAndIncrement());
        });
    }

    @Test
    public void t2(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("sparksync");//sparksync  sinanspeed
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22","admin");

        defaultGraph.getVertices(auth).forEach(e->{
            System.out.println(e);
        });
    }
}
