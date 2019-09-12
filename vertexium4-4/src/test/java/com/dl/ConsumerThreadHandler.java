package com.dl;

import com.wxscistor.config.VertexiumConfig;
import org.vertexium.EdgeBuilderByVertexId;
import org.vertexium.Graph;
import org.vertexium.VertexBuilder;
import org.vertexium.Visibility;
import org.vertexium.accumulo.AccumuloAuthorizations;

import javax.xml.bind.JAXBException;
import java.io.PipedReader;
import java.io.StringReader;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: dl
 * @Date: 2018/12/21 14:32
 * @Version 1.0
 */
public class ConsumerThreadHandler implements Runnable{
    private Graph graph= VertexiumConfig.createAccumuloGraph("batchvertexium2");

    @Override
    public void run() {
        final AccumuloAuthorizations auth = new AccumuloAuthorizations();
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            VertexBuilder vertexBuilder = graph.prepareVertex(Visibility.EMPTY);
            vertexBuilder.setProperty("name","test",Visibility.EMPTY);
            vertexBuilder.setProperty("age",10,Visibility.EMPTY);
            vertexBuilder.save(auth);

            EdgeBuilderByVertexId friend = graph.prepareEdge(r.nextLong() + "", r.nextLong() + "", "friend", Visibility.EMPTY);
            friend.setProperty("size",r.nextInt(100),Visibility.EMPTY);
            friend.save(auth);
        }
        graph.flush();
    }

}
