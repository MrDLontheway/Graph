package com.dl.speed;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeedTest1 {
    private List<String> objectUris = new ArrayList<String>();

    private Random r = new Random();

    public static void main(String[] args) throws InterruptedException {
        Integer threadNumber = Integer.valueOf(args[0]);
        Integer dataNumber = Integer.valueOf(args[1]);
        String database = args[2];
        boolean isIndex = Boolean.valueOf(args[3]);

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(15);
        for (int i = 0; i < threadNumber; i++) {
            fixedThreadPool.execute(new ReciveHandler(database,dataNumber,isIndex));
        }

        while (true){
            Thread.sleep(60000);
        }
    }
}
