package com.com.wxscistor;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.ElementBuilder;
import org.vertexium.Vertex;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.SortDirection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SyncData {

    public SyncData() throws AccumuloSecurityException, AccumuloException {
    }


    @Test
    public void tt1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations rootAuth = AuthUtils.getRootAuth(defaultGraph);
        VertexBuilder vertexBuilder = defaultGraph.prepareVertex(Visibility.EMPTY);
        vertexBuilder.setProperty("createtime",new Date(),new Visibility("ningzongbin"));
        vertexBuilder.save(rootAuth);
        defaultGraph.flush();
    }

    @Test
    public void query() throws AccumuloSecurityException, AccumuloException, InterruptedException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations rootAuth = AuthUtils.getRootAuth(defaultGraph);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
                AccumuloAuthorizations rootAuth = null;
                try {
                    rootAuth = AuthUtils.getRootAuth(defaultGraph);
                } catch (AccumuloSecurityException e) {
                    e.printStackTrace();
                } catch (AccumuloException e) {
                    e.printStackTrace();
                }

                while (true){
                    List<ElementBuilder<Vertex>> vertexBuilders = new ArrayList<>();
                    VertexBuilder vertexBuilder = defaultGraph.prepareVertex(UUID.randomUUID().toString(),Visibility.EMPTY);
                    vertexBuilder.setProperty("createtime",new Date(),new Visibility("ningzongbin"));
                    vertexBuilder.setProperty("createtime",new Date(),new Visibility("zhaoliu"));
                    vertexBuilder.setProperty("createtime",new Date(),new Visibility("003"));

                    vertexBuilders.add(vertexBuilder);
                    defaultGraph.addVerticesAsync(vertexBuilders,rootAuth);
                    defaultGraph.flush();

                    defaultGraph.query(rootAuth)
                            .sort("createtime", SortDirection.ASCENDING).vertices().forEach(x->{
                        System.out.println(x);
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).run();

//        while (true){
//            defaultGraph.query(rootAuth)
//                    .sort("createtime", SortDirection.ASCENDING).vertices().forEach(x->{
//                System.out.println(x);
//            });
//            Thread.sleep(3000);
//        }

    }

    @Test
    public void sync() throws AccumuloSecurityException, AccumuloException {



//        defaultGraph.query(rootAuth)
//                .sort("createtime", SortDirection.ASCENDING).vertices().forEach(x->{
//            System.out.println(x);
//        });
    }
}
