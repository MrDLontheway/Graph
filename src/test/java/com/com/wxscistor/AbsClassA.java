package com.com.wxscistor;

import com.dl.DiyGraph;
import com.dl.QuerTest;
import com.wxscistor.config.VertexiumConfig;
import org.vertexium.Graph;

import java.io.Serializable;
import java.util.Random;

public abstract class AbsClassA implements Serializable {
    public String name = "123";
    public transient Graph graph = VertexiumConfig.defaultGraph;
    private transient DiyGraph diyGraph = new DiyGraph();
    public final transient QuerTest querTest = new QuerTest();
    AbsClassA(Graph graph,DiyGraph diyGraph,QuerTest querTest){
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        while (true){
            int i = random.nextInt(10000);
            System.out.println("echo ================"+i);
            Thread.sleep(1000);
        }
    }
}
