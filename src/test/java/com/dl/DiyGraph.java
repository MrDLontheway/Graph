package com.dl;

import com.wxscistor.config.AccumuloConnector;
import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.util.FieldUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.log4j.Level;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.accumulo.AccumuloVertex;
import org.vertexium.elasticsearch5.Elasticsearch5SearchIndex;
import org.vertexium.elasticsearch5.ElasticsearchSearchIndexConfiguration;
import org.vertexium.elasticsearch5.ElasticsearchSearchQueryBase;
import org.vertexium.query.*;
import org.vertexium.search.IndexHint;
import org.vertexium.search.SearchIndex;
import org.vertexium.util.VertexiumLogger;

import java.io.*;
import java.util.*;

/**
 * @Author: dl
 * @Date: 2018/12/5 10:24
 * @Version 1.0
 */
public class DiyGraph {
    @Test
    public void geo() {
//        if (geoList != null && geoList.size() > 0) {
//            booQueryBuilder.must(QueryBuilders.geoPolygonQuery(gisProp + ".location", geoList).ignoreUnmapped(true));
//        }
//        // gis-圆形查询条件
//        if (point != null) {
//            booQueryBuilder.must(QueryBuilders.geoDistanceQuery(gisProp + ".location").point(point)
//                    .distance(distance, DistanceUnit.KILOMETERS).ignoreUnmapped(true));
//        }

        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations();

        /*VertexBuilder dltest = defaultGraph.prepareVertex("dltest", Visibility.EMPTY);
        dltest.setProperty("com.dltest.pro.name","123哈哈哈哈",Visibility.EMPTY);
        dltest.save(auth);
        defaultGraph.flush();*/

        SearchIndex searchIndex = defaultGraph.getSearchIndex();
        Elasticsearch5SearchIndex searchIndex1;
        if (searchIndex instanceof Elasticsearch5SearchIndex) {
            searchIndex1 = (Elasticsearch5SearchIndex) searchIndex;
//            Vertex mev1 = defaultGraph.getVertex("mev1000", auth);
//            mev1.setProperty("location",new GeoPoint(31.888888888, 120.888888888),Visibility.EMPTY,auth);
//            searchIndex1.addElement(defaultGraph,mev1,auth);
        }
        ElementBuilder<Vertex> vertexElementBuilder = defaultGraph.prepareVertex("testdl", Visibility.EMPTY)
                .setProperty("com.scistor.property.jijiandizhi", new org.vertexium.type.GeoPoint(18.0023, 67.0089),new Visibility("fjjtest"))
//                .setProperty("com.scistor.property.jijiandizhi", new org.vertexium.type.GeoPoint(18.55555, 67.0089), Visibility.EMPTY)
                ;
//                .setProperty("com_scistor_property_dltestdizhi2", new org.vertexium.type.GeoPoint(18.0023, 67.0089), Visibility.EMPTY)
//                .setProperty("com-scistor-property-dltestdizhi", new org.vertexium.type.GeoPoint(18.0023, 67.0089), Visibility.EMPTY);
        Vertex fjjtest = vertexElementBuilder.save(new AccumuloAuthorizations("fjjtest"));
        defaultGraph.flush();
        GraphQuery query = defaultGraph.query(new AccumuloAuthorizations("fjjtest"));
        if(query instanceof ElasticsearchSearchQueryBase){
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //com.scistor.property.jijiandizhi
//            String[] fjjtests = FieldUtils.getEsFieldNames(defaultGraph, "com.scistor.property.jijiandizhi", "fjjtest");
            String[] fjjtests = FieldUtils.getEsFieldNames(defaultGraph, "com.scistor.property.jijiandizhi", "fjjtest");
            for (String s:fjjtests){
                GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery(s).point(new GeoPoint(18.001, 67.002))
                        .distance(5000, DistanceUnit.KILOMETERS)
                        .ignoreUnmapped(true);
//                ((ElasticsearchSearchQueryBase) query).addQueryBuilder(geoDistanceQueryBuilder);
                boolQueryBuilder.should(geoDistanceQueryBuilder);
            }
            ((ElasticsearchSearchQueryBase) query).addQueryBuilder(boolQueryBuilder);
        }
        query.vertices().forEach(x -> {
            System.out.println(x.getId());
        });

//        VertexBuilder mev11 = defaultGraph.prepareVertex("mev33",Visibility.EMPTY);
//        mev11.setProperty("location","31.888888888,120.888888888",Visibility.EMPTY);
//        mev11.save(auth);
//        mev11.setProperty("te",123,Visibility.EMPTY);
//        mev11.save(auth);
//        Vertex mev1 = defaultGraph.addVertex("mev1000", Visibility.EMPTY, auth);
//        Vertex mev1 = defaultGraph.addVertex("mev1", Visibility.EMPTY, auth);
//        Vertex mev13 = defaultGraph.addVertex("mev13", Visibility.EMPTY, auth);
        defaultGraph.flush();
    }

    @Test
    public void writeSampleData() {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("vis1", "vis22", "vis33", "testvis");
        Random r = new Random();
        String[] ads = new String[]{"无锡", "北京", "上海", "苏州", "南京"};
        String[] areas = new String[]{
                "31.4914074718,120.3453540802",
                "31.4737671593,120.3252696991",
                "31.4545859514,120.2265644073",
                "31.5042147428,120.0727558136",
                "39.9046900000,116.4071700000",
                "31.2303700000,121.4737000000",
                "31.2983400000,120.5831900000",
                "32.0583800000,118.7964700000",
                "32.0480246433,118.7374019623"
        };

        String[] articles = new String[]{
                "用一巷的情愁砚墨成淡淡的忧伤，沁染丈宣，洒尽雨滴，描摹了三生五世的雨巷的风云。青雨洒洒，裙丝融融，屋檐下的黄灯，闪烁着秋零木落的哀愁，卷落了雨巷阵阵寒凉。素颜的花，冰脆的枝，轻吟着水雾里情愁，缭绕着漫雾的雨巷。",
                "先画上一个太阳，这样就会感觉到温暖;再画上美丽的花朵，自己仿佛又回到了春天;最后画上各种各样的动物，这一片雪地就变成了大型动物园。我似乎看到了动物们在欢快的奔跑，想着想着我也不知不觉奔跑起来，而脚下软绵绵的雪变成了云彩，我乘着它在天空中飞行。向下面俯视这个世界，一切都变得那么渺小，就连心中的烦恼也变得微不足道。",
                "夜幕降临，霓虹闪烁，雪随风舞，昏暗的路灯下，犹如天女散花，纷飞零乱，席来丝丝寒凉;这雪，混沌了天地，浪漫着人间。今夜的雪，落寞了旅途，寂寥了归人，点透了离人;夜色中，仰首白雪满眉眼，俯首飞絮盈白头。好一场雪，大朵、小朵，千朵、万朵，才会有古时柳宗元的：“千山鸟飞绝，万径人综灭。孤舟蓑衣翁，独钓寒江雪”。",
                "出家，对于某些人，或许才是最佳的选择。那些经历人世间大苦大悲的人，才明白割舍的自由。至今还记得落魄的贾宝玉，披着斗篷，在大雪里越走越远的背影，好似一部大剧悄然落幕，又像一个个普普通通的生命就此凋零。出家并不值得伤悲，它只是另一种生活方式，每个人都有选择过怎么样生活的权利，只要她开心就好，即使出家，也该替她高兴，虽然这个尘世少了一个可爱的小鸽子，但她却在另一片净土，感受别样的春华秋实。",
                "成都是一座慢城，只有慢下来，才会发现属于它的美。它的道路两旁几乎都有银杏树，不知为何，特别喜欢有银杏树的城市。可能是钟情秋天的原因，而银杏黄叶，正好装点了秋色。在成都的街头走一走，直到华灯初上，你会更喜欢它的夜色。"
        };
        Visibility[] vs = new Visibility[]{
                new Visibility("vis1"),
                new Visibility("vis22"),
                new Visibility("vis22&33"),
                new Visibility("vis22|vis1"),
                new Visibility("(vis22&testvis)|vis1")
        };
        //"vis1","vis22","vis33","testvis"
        Visibility v1 = new Visibility("vis1");
        //insert
//        for (int i = 0; i < 20; i++) {
////            Vertex mev11 = defaultGraph.getVertex("mev" + i,auth);
////            VertexBuilder mev11 = defaultGraph.prepareVertex("mev" + i, vs[r.nextInt(vs.length)]);
////            mev11.setProperty("name", "代乐" + i, vs[r.nextInt(vs.length)]);
////            mev11.setProperty("age", r.nextInt(100), vs[r.nextInt(vs.length)]);
////            mev11.setProperty("address", ads[r.nextInt(ads.length)], vs[r.nextInt(vs.length)]);
////            mev11.setProperty("addtime", new Date(), vs[r.nextInt(vs.length)]);
////            mev11.setProperty("location", areas[r.nextInt(areas.length)],  vs[r.nextInt(vs.length)]);
//
//            VertexBuilder mev11 = defaultGraph.prepareVertex("mev" + i, Visibility.EMPTY);
//            mev11.setProperty("name", "代乐" + i, Visibility.EMPTY);
//            mev11.setProperty("age", r.nextInt(100), Visibility.EMPTY);
//            mev11.setProperty("address", ads[r.nextInt(ads.length)], Visibility.EMPTY);
//            mev11.setProperty("addtime", new Date(),Visibility.EMPTY);
//            mev11.setProperty("location", areas[r.nextInt(areas.length)],  Visibility.EMPTY);
//            mev11.setProperty("article", articles[r.nextInt(articles.length)], Visibility.EMPTY);
//            mev11.save(auth);
//        }

//        update
        for (int i = 0; i < 20; i++) {
            Vertex mev11 = defaultGraph.getVertex("mev" + i, auth);
            AccumuloVertex mev111 = (AccumuloVertex) mev11;
            mev111.setProperty("name", "代乐" + i, vs[r.nextInt(vs.length)], auth);
            mev111.deleteProperty(null, "mame", auth);
//            mev11.setProperty("name", "代乐" + i, vs[r.nextInt(vs.length)],auth);
//            mev11.setProperty("age", r.nextInt(100), vs[r.nextInt(vs.length)],auth);
//            mev11.setProperty("address", ads[r.nextInt(ads.length)], vs[r.nextInt(vs.length)],auth);
//            mev11.setProperty("addtime", new Date(), vs[r.nextInt(vs.length)],auth);
//            mev11.setProperty("location", areas[r.nextInt(areas.length)],  vs[r.nextInt(vs.length)],auth);
//            mev11.setProperty("article", articles[r.nextInt(articles.length)], Visibility.EMPTY,auth);
        }

        defaultGraph.flush();
    }

    @Test
    public void testQuery() {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
//        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("storevertexium");
//        AccumuloAuthorizations auth = new AccumuloAuthorizations();
//
//        AccumuloAuthorizations auth = new AccumuloAuthorizations(new String[]{"vis1","vis22","vis33","testvis","fjjtest"});
        AccumuloAuthorizations auth = new AccumuloAuthorizations(new String[]{"fjjtest", "vis1", "vis22", "vis33", "testvis"});
//        AccumuloAuthorizations auth = new AccumuloAuthorizations(CommonUtils.splitString(",","testvis,vis1,vis22,vis33,fjjtest"));

//        String[] testlocations = ((Elasticsearch5SearchIndex) defaultGraph.getSearchIndex()).getAllMatchingPropertyNames(defaultGraph, "testlocation", auth);
//        String[] testlocations1 = ((Elasticsearch5SearchIndex) defaultGraph.getSearchIndex()).getPropertyNames(defaultGraph, "testlocation", auth);
//        System.out.println(testlocations);
        Vertex testGeo = defaultGraph.getVertex("testGeo", auth);
        if (defaultGraph.getSearchIndex() instanceof Elasticsearch5SearchIndex) {
            String visibilityHash = ((Elasticsearch5SearchIndex) defaultGraph.getSearchIndex()).getPropertyVisibilityHashFromPropertyName("testlocation");
            Query query1 = defaultGraph.query(auth)
                    .has("Objecturi", "com.scistor.object.entity.postman");
            query1.vertices().forEach(x -> {
                System.out.println(x);
            });
        }

        System.exit(1);
//        Query query1 = defaultGraph.query(auth)
//                .has("testlocation",GeoCompare.WITHIN,new org.vertexium.type.GeoPoint(34.34,130.34));
//        query1.vertices().forEach(x->{
//            System.out.println(x);
//        });

//        defaultGraph.prepareVertex("testGeo",Visibility.EMPTY)
//                .setProperty("name","test",Visibility.EMPTY)
//                .setProperty("testlocation",new org.vertexium.type.GeoPoint(34.34,130.34),Visibility.EMPTY)
//                .save(auth);

        defaultGraph.flush();
        Elasticsearch5SearchIndex searchIndex1;
        SearchIndex searchIndex = defaultGraph.getSearchIndex();
        if (searchIndex instanceof Elasticsearch5SearchIndex) {
            searchIndex1 = (Elasticsearch5SearchIndex) searchIndex;
            ElasticsearchSearchIndexConfiguration config = searchIndex1.getConfig();
//            QueryResultsIterable<String> strings = defaultGraph.query(auth).vertexIds();
            Query query = defaultGraph.query(auth)
                    .has("Objecttype", "com.scistor.object.event")
//                    .has("ObjectLabel","4119050996")
//                    .has("com.scistor.property.peoplename","何阜")
//                    .has("com.scistor.property.jijianshengfen","河南省")
//                    .has("article", TextPredicate.CONTAINS, "寂寥了归人")
//                    .has()
//                    .has("address", "南京")
//                    .range("age",66,true,200,true)
//                    .sort("addtime", SortDirection.ASCENDING)
                    ;
//            final QueryResultsIterable<? extends VertexiumObject> search1 = query.search(EnumSet.of(VertexiumObjectType.VERTEX), defaultGraph.getDefaultFetchHints());
//            search1.forEach(x -> {
//                System.out.println("vertexID:" + x);
//                x.getProperties().forEach(y->{
//                    System.out.println(y.getName()+":"+y.getValue());
//                });
//            });
//            System.exit(1);

//            if (query instanceof ElasticsearchSearchQueryBase) {
//                ElasticsearchSearchQueryBase q = (ElasticsearchSearchQueryBase) query;
//                QueryResultsIterable<? extends VertexiumObject> search = q.search(EnumSet.of(VertexiumObjectType.VERTEX), defaultGraph.getDefaultFetchHints());
//                System.exit(1);
//            }
            query.vertices().forEach(x -> {
                System.out.println("vertexID:" + x);
            });
            System.exit(1);
            if (query instanceof ElasticsearchSearchQueryBase) {
                ElasticsearchSearchQueryBase v = (ElasticsearchSearchQueryBase) query;
//                v.skip(5);
//                v.limit(12);
                QueryResultsIterable<? extends VertexiumObject> search = v.search(EnumSet.of(VertexiumObjectType.VERTEX), defaultGraph.getDefaultFetchHints());
                Iterator<? extends VertexiumObject> iterator = search.iterator();
                search.forEach(x -> {
                    System.out.println("vertexID:" + x);
                });
                System.out.println(1);
            }
            System.exit(1);
//            GraphQuery dl = searchIndex1.queryGraph(defaultGraph, "代乐", auth);
        }

//        Query has = query.has("name", "代乐");
//        has.vertexIds().forEach(x->{
//            System.out.println(x);
//        });
    }

    public static void seriTest(Object o) {
        // ObjectOutputStream 对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
        try {
            ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                    new File("D:\\_vm\\serio")));
            oo.writeObject(o);
            System.out.println("对象序列化成功！");
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations();
        final Vertex mev12 = defaultGraph.getVertex("mev12", auth);
        QueryBase.HasValueContainer hasValueContainer = new QueryBase.HasValueContainer("article", Compare.EQUAL, 123, defaultGraph.getPropertyDefinitions());
        System.out.println("HasValueContainer=================");
        seriTest(hasValueContainer);
//        System.out.println("Vertex=================");
//        seriTest(mev12);
        System.out.println("VertexiumObjectType.VERTEX=================");
        seriTest(VertexiumObjectType.VERTEX);
    }

    @Test
    public void debugQuery() {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest");
        final Vertex mev12 = defaultGraph.getVertex("mev12", auth);

        boolean f = true;
        while (f) {
            Query has = defaultGraph.query(auth)
                    .has("ObjectLabel", Contains.IN, new String[]{"18860373349", "15143156749"});
//                .has("com.scistor.property.address","黑龙江省哈尔滨市道里区大方路954号");

            has.vertices().forEach(x -> {
                System.out.println(x);
            });
        }
        System.exit(1);
        Elasticsearch5SearchIndex searchIndex1;
        if (defaultGraph.getSearchIndex() instanceof Elasticsearch5SearchIndex) {
            searchIndex1 = (Elasticsearch5SearchIndex) defaultGraph.getSearchIndex();
            GraphQuery q = searchIndex1.queryGraph(defaultGraph, null, auth);
//            q.has("DataSource","213",Predicate);
//            q.has("ObjectLabel","3344567");
            q.has("com.scistor.property.address", "黑龙江省哈尔滨市道里区大方路954号");
            if (q instanceof ElasticsearchSearchQueryBase) {
                ElasticsearchSearchQueryBase v = (ElasticsearchSearchQueryBase) q;
                QueryResultsIterable<? extends VertexiumObject> search = v.search(EnumSet.of(VertexiumObjectType.VERTEX), defaultGraph.getDefaultFetchHints());

                search.forEach(x -> {
                    System.out.println(x.getId());
                });
            }
        }

        System.out.println(mev12);
    }

    @Test
    public void geoquery() {
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations();

        SearchIndex searchIndex = defaultGraph.getSearchIndex();
        Elasticsearch5SearchIndex e5;
        if (defaultGraph.getSearchIndex() instanceof Elasticsearch5SearchIndex) {
            GraphQuery query = defaultGraph.query(auth);
            if (query instanceof ElasticsearchSearchQueryBase) {
                VertexiumLogger queryLogger = ElasticsearchSearchQueryBase.QUERY_LOGGER;
                //VertexiumLoggerFactory.//getQueryLogger(Query.class);
                GeoPoint point1 = new GeoPoint(32.0480246433, 118.7374019623);
                query.has("name", "代乐1");
                query.range("age", 0, 100);
                GeoDistanceQueryBuilder queryBuilder1 = QueryBuilders.geoDistanceQuery("__location").point(point1)
                        .distance(10, DistanceUnit.KILOMETERS).ignoreUnmapped(true);
                ((ElasticsearchSearchQueryBase) query).addQueryBuilder(queryBuilder1);
                query.vertices().forEach(x -> {
                    System.out.println(x);
                });
                System.out.println("finshed========================");
            }

            System.out.println("qyery2===================");

            defaultGraph.query("代乐", auth).vertices().forEach(x -> {
                System.out.println(x);
            });
            System.exit(1);
            e5 = (Elasticsearch5SearchIndex) defaultGraph.getSearchIndex();
            final Client client = e5.getClient();
//            final GraphQuery graphQuery = e5.queryGraph(defaultGraph, "代乐",auth);
//            graphQuery.has("123",123);
//            graphQuery.vertices().forEach(x->{
//                System.out.println(x);
//            });

//            List<GeoPoint> gs = new ArrayList<>();

            GeoPoint point = new GeoPoint(32.0480246433, 118.7374019623);
//            final GeoPolygonQueryBuilder location = QueryBuilders.geoPolygonQuery("__location", gs).ignoreUnmapped(true);

            GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("__location").point(point)
                    .distance(5, DistanceUnit.KILOMETERS).ignoreUnmapped(true);

            BoolQueryBuilder must = QueryBuilders.boolQuery().must(queryBuilder);
            GetRequestBuilder getRequestBuilder = client.prepareGet();
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch("vertexium")
                    .setTypes("e")
                    .setQuery(QueryBuilders.boolQuery().must(queryBuilder))
                    .storedFields(
                            Elasticsearch5SearchIndex.ELEMENT_ID_FIELD_NAME,
                            Elasticsearch5SearchIndex.ELEMENT_TYPE_FIELD_NAME,
                            Elasticsearch5SearchIndex.EXTENDED_DATA_TABLE_NAME_FIELD_NAME,
                            Elasticsearch5SearchIndex.EXTENDED_DATA_TABLE_ROW_ID_FIELD_NAME
                    );
            SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
            SearchHits hits = searchResponse.getHits();
            System.out.println("123");

//
//
//            client.prepareSearch(defaultGraph.getVerticesTableName())
//                    .setQuery(must)
//                    .
        }
    }

    @Test
    public void clearGraph() {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations();
        Iterable<Vertex> vertices = defaultGraph.getVertices(auth);
        vertices.forEach(x -> {
            defaultGraph.deleteVertex(x, auth);
        });
        defaultGraph.getEdges(auth).forEach(x -> {
            defaultGraph.deleteEdge(x, auth);
        });
        defaultGraph.flush();
    }

    @Test
    public void printGraph() {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("vis1", "vis22", "vis33", "testvis");
        AccumuloAuthorizations auth2 = new AccumuloAuthorizations();

        Visibility vis1 = new Visibility("vis1");
        Vertex vertex11 = defaultGraph.prepareVertex("vertex1", Visibility.EMPTY).save(auth);
//        Vertex vertex11 = defaultGraph.getVertex("vertex1", auth);
//        vertex11.setProperty("name","代乐啊",Visibility.EMPTY,auth);
//        Property property = vertex11.getProperty("name");
//        vertex11.deleteProperties("name",auth);
//        vertex11.setProperty("name","代乐哈哈哈哈",vis1,auth);
//        defaultGraph.flush();
//        vertex11.setProperty("name","name1",Visibility.EMPTY,auth);
//        defaultGraph.flush();
//        vertex11.getvis
        boolean vertext = defaultGraph.doesVertexExist("vertex1", auth);
        System.exit(1);
        long time = new Date().getTime();
        for (int i = 0; i < 10000; i++) {
            boolean vertex1 = defaultGraph.doesVertexExist("vertex1", auth);
            if (defaultGraph instanceof AccumuloGraph) {
                AccumuloGraph ag = defaultGraph;
            }
            if (vertex1) {
                defaultGraph.deleteVertex("vertex1", auth);
            }
            defaultGraph.prepareVertex("vertex1", Visibility.EMPTY).save(auth);
        }
        long time1 = new Date().getTime();
        System.out.println((time1 - time) / 10000 + "/ms");
//        Iterable<Vertex> vertices = defaultGraph.getVertices(auth);
//        System.out.println("vertex=======================");
//        vertices.forEach(x->{
//            System.out.println(x.getId()+"   Visibility:"+x.getVisibility());
//            x.getProperties().forEach(y->{
//                System.out.println(y.getName()+":"+y.getValue()+"   Visibility:"+y.getVisibility());
//            });
//        });
//        System.out.println("edge=======================");
//        defaultGraph.getEdges(auth).forEach(x->{
//            System.out.println(x.getId()+"   Visibility:"+x.getVisibility());
//            x.getProperties().forEach(y->{
//                System.out.println(y.getName()+":"+y.getValue()+"   Visibility:"+y.getVisibility());
//            });
//        });
    }


    @Test
    public void tst1() {
        try {
            Process proc = Runtime.getRuntime().exec("jps -l -v");
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mtest() throws IOException {
        String[] s1 = new String[]{"1", "2", "3", "4"};
        String[] insert = new String[]{"0", "2", "4", "6"};

        for (String row : insert) {
            System.out.println(s1.toString());
        }
        String[] auth = Arrays.copyOf(s1, s1.length + 1);
        auth[auth.length - 1] = "last";
        System.out.println(auth);
        Process jps = Runtime.getRuntime().exec("jps");
    }

    @Test
    public void esT(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.queryStringQuery("123"));

        GeoPoint point = new GeoPoint(22.32, 112.3);
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("123_gp").point(point)
                .distance("0.6", DistanceUnit.KILOMETERS).ignoreUnmapped(true);
        GeoDistanceQueryBuilder point1 = geoDistanceQueryBuilder.point(new GeoPoint(22.22222, 112.22222));
        GeoDistanceQueryBuilder distance = point1.distance("22.6");

        double distance2 = geoDistanceQueryBuilder.distance();
        boolQueryBuilder.must(geoDistanceQueryBuilder);
        boolQueryBuilder.should(distance);
        double distance1 = point1.distance();

        System.out.println(boolQueryBuilder.toString());
    }

    @Test
    public void tt1() throws AccumuloSecurityException, AccumuloException {
        //1w 132s
//        for (int i = 0; i < 10000; i++) {
//            String root = AccumuloConnector.addAuths("root", "fjjtest,111111");
//        }
        AccumuloGraph properties = VertexiumConfig.createAccumuloGraph("dlvertexium");
        ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
        for (int i = 0; i < 12000; i++) {
            VertexBuilder vertexBuilder = properties.prepareVertex(Visibility.EMPTY);
            vertexBuilder.setProperty("com.dl.property.age",11,Visibility.EMPTY);
            vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
            vs.add(vertexBuilder);
        }
        properties.getSearchIndex().addElements(properties,properties.addVertices(vs,new AccumuloAuthorizations()),new AccumuloAuthorizations());
        properties.flush();
    }

    @Test
    public void tt2() throws AccumuloSecurityException, AccumuloException {
        //1w 132s
//        for (int i = 0; i < 10000; i++) {
//            String root = AccumuloConnector.addAuths("root", "fjjtest,111111");
//        }
        AccumuloGraph properties = VertexiumConfig.createAccumuloGraph("dlvertexium");
        ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
        for (int i = 0; i < 12000; i++) {
            VertexBuilder vertexBuilder = properties.prepareVertex(Visibility.EMPTY);
            vertexBuilder.setProperty("com.dl.property.age",11,Visibility.EMPTY);
            vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
            vs.add(vertexBuilder);
        }
        properties.getSearchIndex().addElements(properties,properties.addVertices(vs,new AccumuloAuthorizations()),new AccumuloAuthorizations());
        properties.flush();
    }

    @Test
    public void quertmp(){
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest");

//        EdgeBuilderByVertexId edgeBuilderByVertexId = defaultGraph.prepareEdge("myedge", "tmp1", "tmp2", "heihei", Visibility.EMPTY);
//        edgeBuilderByVertexId.save(auth);
//        defaultGraph.deleteEdge("myedge",auth);
//        defaultGraph.flush();

        GraphQuery query = defaultGraph.query(auth);
        query.has("com.dltest.pro.name", Contains.IN, new String[]{"123哈哈哈哈"});
        SearchResponse seq = null;
        seq.getHits().getTotalHits();
        query.vertices().forEach(x->{
            System.out.println(x);
        });
    }
}
