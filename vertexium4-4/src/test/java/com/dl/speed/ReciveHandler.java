package com.dl.speed;

import com.wxscistor.config.VertexiumConfig;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.ElementBuilder;
import org.vertexium.accumulo.AccumuloGraph;

import java.util.*;


public class ReciveHandler implements Runnable{
    private AccumuloGraph graph = null;
    private int vnum = 0;
    ReciveHandler(String graphName, int vnum){
        graph = VertexiumConfig.createAccumuloGraph(graphName);
    }

    private List<String> objectUris = new ArrayList<String>();
    private List<String> linkUris = new ArrayList<String>();


    private Random r = new Random();

    public <T>T getRandomeValue(List<T> data){
        T datum = data.get(r.nextInt(data.size()));
        return datum;
    }

    public void inint(){
        objectUris.addAll(Arrays.asList(new String[]{"people","targetpeople","article","message","phoneNumber","phone"}));
        linkUris.addAll(Arrays.asList(new String[]{"朋友","发送","使用","亲人","关注"}));
    }

    @Override
    public void run() {
        inint();
        ArrayList<org.vertexium.ElementBuilder<Vertex>> vertexBuilders = new ArrayList<>();
        Visibility fjjtest = new Visibility("fjjtest");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        for (int i = 0; i < vnum; i++) {
            String uri = getRandomeValue(objectUris);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            VertexBuilder vertexBuilder = graph.prepareVertex(uri + uuid, Visibility.EMPTY);
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

        ArrayList<ElementBuilder<Edge>> edgeBuilders = new ArrayList<>();
        //生成随机边
        for (int i = 0; i < vnum/3; i++) {
            String out = getRandomeValue(vertexBuilders).getElementId();
            String in = getRandomeValue(vertexBuilders).getElementId();
            EdgeBuilderByVertexId builder = graph.prepareEdge(out, in, getRandomeValue(linkUris), Visibility.EMPTY);
            builder.setProperty("longtime",new Date().getTime(),fjjtest);
            builder.setProperty("start_time",new Date(),fjjtest);
            builder.setProperty("end_time",new Date(),fjjtest);
            edgeBuilders.add(builder);
        }
        graph.addVerticesAsync(vertexBuilders,auth);
        graph.addEdgesAsync(edgeBuilders,auth);
        graph.flush();
    }
}
