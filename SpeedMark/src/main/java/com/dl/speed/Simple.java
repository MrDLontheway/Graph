package com.dl.speed;

import org.apache.accumulo.core.client.Durability;

public class Simple {
    public static void main(String[] args) {
        Integer threadNumber = Integer.valueOf(args[0]);
        Integer dataNumber = Integer.valueOf(args[1]);
        String database = args[2];
        boolean isIndex = Boolean.valueOf(args[3]);
        ReciveHandler reciveHandler = new ReciveHandler(database, dataNumber, isIndex);
        reciveHandler.run();
    }
}
