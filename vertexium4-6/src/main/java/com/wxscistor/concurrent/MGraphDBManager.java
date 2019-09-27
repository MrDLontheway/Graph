package com.wxscistor.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wxscistor.config.VertexiumConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.ElementBuilder;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.search.IndexHint;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.concurrent.*;

public class MGraphDBManager implements Serializable {
    private static AccumuloGraph accumuloGraph = null;

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("Concurrent Thread bulk data worker-%d")
            .build();

    public static ExecutorService executeBulk = Executors.newCachedThreadPool(threadFactory);

    public static LinkedList<AccumuloGraph> connPool = new LinkedList<AccumuloGraph>();

    public synchronized static AccumuloGraph getAccumuloGraph(String tableName) {
        //解决netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        org.apache.log4j.Logger logger = Logger.getLogger("org.vertexium");
        logger.setLevel(Level.INFO);
        if (accumuloGraph == null) {
            System.out.println("======================================================================================================");
            System.out.println("==================================== create AccumuloGraph=============================================");
            System.out.println("======================================================================================================");
            accumuloGraph = VertexiumConfig.createAccumuloGraph(tableName);
            if(accumuloGraph.getSearchIndex() instanceof Elasticsearch5SearchIndex){
                ((Elasticsearch5SearchIndex) accumuloGraph.getSearchIndex()).ensureIndexCreatedAndInitialized(tableName);
            }
        }
        return accumuloGraph;
    }


    public static void init(int poolSize, String tableName) {
        for (int i = 0; i < poolSize; i++) {
            connPool.add(VertexiumConfig.createAccumuloGraph(tableName));
        }
        System.out.println("MGraphDBManager init over");
    }

    public static void main(String[] args) {
        String graphName = args[0];
        Integer batchSize = Integer.valueOf(args[1]);
        Integer howBatch = Integer.valueOf(args[2]);
        boolean index = true;
        if (args[3] != null) {
            if ("noindex".equals(args[3])) {
                index = false;
            }
        }
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph(graphName);
        if (args[4] != null) {
            if ("vertex".equals(args[4])) {
                for (int j = 0; j < howBatch; j++) {
                    ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
                    for (int i = 0; i < batchSize; i++) {//1E 条点 数据
                        VertexBuilder vertexBuilder = most.prepareVertex(Visibility.EMPTY);
                        Map<String, Object> address = RandomValue.getAddress();
                        address.forEach((x, y) -> {
                            vertexBuilder.setProperty(x, y, Visibility.EMPTY);
                        });
                        vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
                        vs.add(vertexBuilder);
                    }
                    Iterable<Vertex> vertices = most.addVertices(vs, new AccumuloAuthorizations());
                    if (index) {
                        most.getSearchIndex().addElements(most, vertices, new AccumuloAuthorizations());
                    }
                    most.flush();
                    System.out.println("write " + batchSize + "elements=====");
                }
            } else if ("edge".equals(args[4])) {
                insertBatchEdge(most);
            }
        }


    }

    private static void insertBatchEdge(AccumuloGraph graph) {
        String[] linkType = new String[]{"friend", "like", "forward", "dislike", "post", "phone", "message"};
        ArrayList<Range> ranges = new ArrayList<>();
        ranges.add(new Range("0", "5499627842"));
        ranges.add(new Range("5499627842", "54996587604"));
        ranges.add(new Range("54996587604", "54996866472"));
        ranges.add(new Range("54996866472", "54997172122"));
        ranges.add(new Range("54997172122", "55001008896"));
        ranges.add(new Range("55001008896", "55001258893"));
        ranges.add(new Range("55001258893", "55001485951"));
        ranges.add(new Range("55001485951", "55001694621"));
        ranges.add(new Range("55001694621", "55001964575"));
        ranges.add(new Range("55001964575", "55002196486"));
        ranges.add(new Range("55002196486", "" + Long.MAX_VALUE));
        Random random = new Random();
        for (Range x : ranges) {
            List<String> ids = new ArrayList<>();
            Iterable<Vertex> inRanges = graph.getVerticesInRange(x, new AccumuloAuthorizations());
            inRanges.forEach(y -> {
                ids.add(y.getId());
            });
            ArrayList<EdgeBuilderByVertexId> es = new ArrayList<>();
            AccumuloAuthorizations accumuloAuthorizations = new AccumuloAuthorizations();
            for (int i = 0; i < 2000000; i++) {
                String out = ids.get(random.nextInt(ids.size()));
                String in = ids.get(random.nextInt(ids.size()));
                EdgeBuilderByVertexId edge = graph.prepareEdge(out, in, linkType[random.nextInt(linkType.length)], Visibility.EMPTY);
                edge.setIndexHint(IndexHint.DO_NOT_INDEX);
                edge.setProperty("edgeAddress", RandomValue.getRoad(), Visibility.EMPTY);
                edge.setProperty("number", RandomValue.getNum(1, 1000), Visibility.EMPTY);
                edge.setProperty("startDate", RandomValue.randomDate("1996-01-01", "2019-01-01"), Visibility.EMPTY);
                edge.save(accumuloAuthorizations);
            }
            graph.flush();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static synchronized AccumuloGraph getConnection() {
        if (connPool.size() > 0) {
            final AccumuloGraph conn = connPool.removeFirst();
            return conn;
        } else {
            throw new VertexiumException("图库繁忙，稍后再试");
        }
    }

    public static synchronized void close(AccumuloGraph graph) {
        connPool.addLast(graph);
    }

    public static void asyncBulkData(AccumuloGraph graph, AccumuloAuthorizations auth, Iterable<org.vertexium.ElementBuilder<Element>> data) {
        executeBulk.execute(new SaveDataHandler(graph,auth,data));
    }

    public static void tmpM(){
        System.out.println("tmpM");
    }
}
