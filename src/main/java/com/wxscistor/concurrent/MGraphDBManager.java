package com.wxscistor.concurrent;

import com.wxscistor.config.VertexiumConfig;
import org.vertexium.accumulo.AccumuloGraph;

public class MGraphDBManager {
    private static AccumuloGraph accumuloGraph = null;

    public synchronized static AccumuloGraph getAccumuloGraph(String tableName){
            if(accumuloGraph==null){
                System.out.println("======================================================================================================");
                System.out.println("==================================== create AccumuloGraph=============================================");
                System.out.println("======================================================================================================");
                accumuloGraph = VertexiumConfig.createAccumuloGraph(tableName);
            }
        return accumuloGraph;
    }
}
