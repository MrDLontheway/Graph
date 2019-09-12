package com.wxscistor.config;

import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.accumulo.AccumuloGraphConfiguration;
import org.vertexium.util.VertexiumLogger;
import org.vertexium.util.VertexiumLoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: dl
 * @Date: 2018/11/8 15:51
 * @Version 1.0
 *
 * 图库配置
 */
public class VertexiumConfig {
    private static final VertexiumLogger LOGGER = VertexiumLoggerFactory.getLogger(VertexiumConfig.class);
    public static Map vertexiumConfig = new HashMap<>();
    public static Properties properties = new Properties();
    public static String TABLE_NAME_PREFIX = AccumuloGraphConfiguration.DEFAULT_TABLE_NAME_PREFIX;
    public static AccumuloGraph defaultGraph;
    public static String rootAuth;
    public static Map setTableName(String tableName) {
        TABLE_NAME_PREFIX = tableName;
        Map clone = new HashMap();
        clone.putAll(vertexiumConfig);
        clone.put(AccumuloGraphConfiguration.TABLE_NAME_PREFIX, TABLE_NAME_PREFIX);
        clone.put("search.indexName", tableName);
        clone.put("search.extendedDataIndexNamePrefix", tableName+"_extdata_");
        return clone;
    }

    static {
        try {
            InputStream in = VertexiumConfig.class.getClassLoader().getResourceAsStream("vertexiumConf.properties");
            properties.load(in);
            vertexiumConfig = ((Map) properties);
//            AccumuloGraphConfiguration graphConfig = new AccumuloGraphConfiguration(vertexiumConfig);
//            defaultGraph = createAccumuloGraph(vertexiumConfig.getOrDefault("TABLE_NAME_PREFIX",TABLE_NAME_PREFIX).toString());//AccumuloGraph.create(graphConfig);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("vertexiumConf init load failed !",e);
        }
    }

    public void setConfig(HashMap config){
        vertexiumConfig = config;
    }

    public static AccumuloGraph createAccumuloGraph(){
        AccumuloGraphConfiguration graphConfig = new AccumuloGraphConfiguration(vertexiumConfig);
        AccumuloGraph graph = AccumuloGraph.create(graphConfig);
        return graph;
    }

    public static AccumuloGraph createAccumuloGraph(String tableName){
        Map config = VertexiumConfig.setTableName(tableName);
        return createAccumuloGraph(config);
    }


    public static AccumuloGraph createAccumuloGraph(Map config){
        AccumuloGraphConfiguration graphConfig = new AccumuloGraphConfiguration(config);
        AccumuloGraph graph = AccumuloGraph.create(graphConfig);
        return graph;
    }

    public AccumuloGraph getAccumuloGraph(){
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        return graph;
    }

    public static AccumuloGraph createAccumuloGraphWithNoSeach(String tableName){
        Map config = VertexiumConfig.setTableName(tableName);
        Iterator<Map.Entry<String, Object>> it = config.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getKey().startsWith("search"))
                it.remove();
        }
        AccumuloGraphConfiguration graphConfig = new AccumuloGraphConfiguration(config);
        AccumuloGraph graph = AccumuloGraph.create(graphConfig);
        return graph;
    }
}
