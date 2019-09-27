package com.dl.speed;

import com.wxscistor.concurrent.MGraphDBManager;
import com.wxscistor.config.VertexiumConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;

import java.util.*;
import java.util.concurrent.*;

public class SpeedTest1 {
    private List<String> objectUris = new ArrayList<String>();
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    public static ExecutorService execute = new ThreadPoolExecutor(3, 5,
            0L, TimeUnit.MILLISECONDS,
            queue,
            Executors.defaultThreadFactory());

    private Random r = new Random();

    public static void main(String[] args) throws InterruptedException {
        Logger logger = Logger.getLogger("org.vertexium");
        logger.setLevel(Level.INFO);

        Logger eslog = Logger.getLogger("org.elasticsearch.client.transport.TransportClient");
        eslog.setLevel(Level.DEBUG);

        System.out.println(new Date().toLocaleString());
        Integer threadNumber = Integer.valueOf(args[0]);
        Integer dataNumber = Integer.valueOf(args[1]);
        String database = args[2];
        boolean isIndex = Boolean.valueOf(args[3]);

        Integer dbconnnumber = Integer.valueOf(args[4]);

        MGraphDBManager.init(dbconnnumber,database);

        AccumuloGraph rollover = VertexiumConfig.createAccumuloGraph(database);
//        if(rollover.getSearchIndex() instanceof Elasticsearch5SearchIndex){
//            ((Elasticsearch5SearchIndex) rollover.getSearchIndex()).ensureIndexCreatedAndInitialized(database);
//        }

//        AccumuloGraph accumuloGraph = VertexiumConfig.createAccumuloGraph(database);

        for (int i = 0; i < threadNumber; i++) {
            fixedThreadPool.execute(new ReciveHandler(rollover,dataNumber,isIndex));
        }

//        LinkedBlockingQueue<Runnable> tmpqueue = new LinkedBlockingQueue<>();
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 15,
//                0L, TimeUnit.MILLISECONDS,tmpqueue
//                ,
//                Executors.defaultThreadFactory());
//
//        for (int i = 0; i < 100; i++) {
//            threadPoolExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println(Thread.currentThread().getName()+" running....."+new Date().toLocaleString());
//                        System.out.println(Thread.currentThread().getName()+" exit ...");
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }

//
//
//        new Thread(new ReciveHandler(accumuloGraph,dataNumber,isIndex)).run();
//        while (true){
//            Thread.sleep(60000);
//        }
    }
}
