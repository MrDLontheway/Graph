package com.dl.Relat;

import com.com.wxscistor.Child;
import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.pojo.vertexium.GraphRelation;
import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloElement;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.accumulo.AccumuloVertex;
import org.vertexium.query.*;
import org.vertexium.type.GeoPoint;
import org.vertexium.type.GeoPolygon;

import java.io.*;
import java.security.PublicKey;
import java.sql.Connection;
import java.time.Year;
import java.util.*;

public class Relat implements Serializable {
    public transient AccumuloGraph connection= VertexiumConfig.defaultGraph;
    public String name = "123";
    @Test
    public void rr1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations rootAuth = AuthUtils.getRootAuth(defaultGraph);
//
//        Vertex v = defaultGraph.getVertex("ENTITY_TWITTER_USER_a3d99fd3c9eec288b444694d1481773b", rootAuth);
//        GeoPoint geoPoint = new GeoPoint(39.40430251037351, 35.366778334839864);
//        v.setProperty("com.geoaddress",geoPoint,new Visibility("admin"),rootAuth);
//        defaultGraph.flush();
//        v.deleteProperty(null,"com.scistor.property.followed_counts",rootAuth);
//        defaultGraph.flush();
        /**
         * {
         * 		"longitude": 34.410967787964864,
         * 		"latitude": 39.920205708944884
         *        }, {
         * 		"longitude": 34.410967787964864,
         * 		"latitude": 38.58031423846286
         *    }, {
         * 		"longitude": 38.052935561402364,
         * 		"latitude": 38.58031423846286
         *    }, {
         * 		"longitude": 38.052935561402364,
         * 		"latitude": 39.920205708944884
         *    }
         */
        List<org.vertexium.type.GeoPoint> geoList = new ArrayList<org.vertexium.type.GeoPoint>();
        org.vertexium.type.GeoPoint point = null;
        geoList.add(new GeoPoint(40.665964879040516, 31.473908286605138));
        geoList.add(new GeoPoint(39.1832108788996, 31.473908286605138));
        geoList.add(new GeoPoint(39.1832108788996, 35.41250691941764));
        geoList.add(new GeoPoint(40.665964879040516, 35.41250691941764));
        geoList.add(new GeoPoint(40.665964879040516, 31.473908286605138));


        GeoPolygon geoPolygon = new GeoPolygon(geoList);
//        geoPolygon.validate();
        Query geoaddress = defaultGraph.query(rootAuth).has("com.scistor.property.person_register_address", GeoCompare.WITHIN,geoPolygon);
        geoaddress.vertices().forEach(row->{
            System.out.println(row);
        });
    }
    @Test
    public void r1(){
        AccumuloGraph defaultGraph = VertexiumConfig.createAccumuloGraph("vertexium");
        AccumuloAuthorizations auth = new AccumuloAuthorizations("fjjtest","vis1","vis22");
        String[] ids = new String[]{"ENTITY_POSTMAN_5daf5746ffcf1253b616158b2db09452",
                "EVENT_POSTEVENT_599144ded8d5b8682340f37e02e0a939",
                "ENTITY_POSTMAN_20db3825707a8673b8c059038949c851"};
        List<String> strings = Arrays.asList(ids);
        Iterable<RelatedEdge> relatedEdgeSummary = defaultGraph.findRelatedEdgeSummary(strings, auth);
        relatedEdgeSummary.forEach(x->{
            Vertex vertex = defaultGraph.getVertex(x.getOutVertexId(), auth);
            Edge edge = defaultGraph.getEdge(x.getEdgeId(), auth);
            System.out.println(edge);
        });
        Iterable<String> relatedEdgeIds = defaultGraph.findRelatedEdgeIds(strings, auth);
        relatedEdgeIds.forEach(x->{
            System.out.println(x);
        });
    }

    @Test
    public void r2(){
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22","admin");

//        Edge edge = defaultGraph.getEdge("EVENT_FACEBOOK_POST_8c9b3816e23e8581d6abc092924935aa", auth);
//        edge.setProperty("com.dl.tmp.pros.t1","123456",new Visibility("knowledgebase_kafka"),auth);
//        defaultGraph.flush();
        PropertyDefinition pdf = defaultGraph.getPropertyDefinition("com.scistor.property.tags");
//        defaultGraph.savePropertyDefinition(new PropertyDefinition(pdf.getPropertyName(),pdf.getDataType(), EnumSet.of(TextIndexHint.FULL_TEXT),pdf.getBoost(),true));
//        defaultGraph.flush();
        HistogramAggregation aggregation = new HistogramAggregation("groupbydate", "TimeStart", "1M", 0L);
        Date date1 = new Date(0, 1, 1);
        Date date = new Date();
        HistogramAggregation.ExtendedBounds<Date> dateExtendedBounds = new HistogramAggregation.ExtendedBounds<Date>(date1,date);
        HistogramAggregation.ExtendedBounds<String> dateExtendedBounds2 = new HistogramAggregation.ExtendedBounds<String>("1990-01","1990-12");

        aggregation.setExtendedBounds(dateExtendedBounds2);
        Query query = defaultGraph.query(auth)
                .addAggregation(aggregation)
                .has("TimeStart");
        HistogramResult groupbydate = query.edges().getAggregationResult("groupbydate", HistogramResult.class);
//        Query objecturi = defaultGraph.query(auth)
//                .has("Objecturi", "com.scistor.object.event.twitter_post");
        query.vertices().forEach(x->{
            Property property = x.getProperty("com.scistor.property.time_interval.start");
            Object value = property.getValue();
            System.out.println(x);
        });

//        VertexBuilder test1111 = defaultGraph.prepareVertex("test1111", Visibility.EMPTY);
//        org.elasticsearch.common.geo.GeoPoint
//        ElementBuilder<Vertex> des = test1111.setProperty("com.dl.pros", new GeoPoint(10.11, 12.33, "desaaa"), Visibility.EMPTY);
//        Vertex save = des.save(auth);
//        defaultGraph.flush();
//        GraphQuery query = defaultGraph.query("*150*", auth);
//        query.vertices().forEach(x->{
//            System.out.println(x);
//        });
    }

    @Test
    public void tmp2(){
        Properties properties = System.getProperties();
        String java = properties.getProperty("java.home");
        String env = properties.getProperty("env.JAVA_HOME");

        System.out.println(java);
        System.out.println(env);
    }

    @Test
    public void tmp3() throws IOException, ClassNotFoundException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations auth = new AccumuloAuthorizations("knowledgebase_kafka","fjjtest","vis1","vis22","admin");

        AccumuloVertex next = (AccumuloVertex) defaultGraph.getVertices(auth).iterator().next();
        String path1 = AccumuloElement.class.getResource("/").getPath();
        System.out.println(path1);

        Child r = new Child(null,null,null);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("vertex"));
        outputStream.writeObject(next);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream vertex = new ObjectInputStream(new FileInputStream(
                new File("vertex")));
        AccumuloVertex o = (AccumuloVertex) vertex.readObject();

        Date date = new Date(189273600000L);
        String s = date.toLocaleString();
        System.out.println(date);
    }


    @Test
    public void edgeconfirm() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
        AccumuloAuthorizations rootAuth = AuthUtils.getRootAuth(defaultGraph);
        String[] rowks = new String[]{"ENTITY_TWITTER_USER_61d2e0d98c299f68540ab2db8731429e"};
        String[] toRowks = new String[]{"ENTITY_TWITTER_USER_82db2231b98517f0f7c2f0e95a83efb1","ENTITY_FACEBOOK_USER_a044d6923febf79c7c2fba097cad84d9","ENTITY_TWITTER_USER_65b7cfbfa590183364959d456d399a5c","EVENT_WEIBO_POST_b2eb635c53ccc988093249a9f322c1d9","ENTITY_IMPORTANT_PERSON_c034d8f6e44158d2f73fb70ebf9392ac","ENTITY_ZZZZ_60ae7e134c0027b06b7711e871017be7","DOCUMENT_DOCUMENT_01_f7ecc68a37774653a77cb69cbce054e2","MEDIA_MEDIA_01_e46b67b87cb846db9c7e357ac8cb45eb","EVENT_TWITTER_POST_af5e0d2a75ac19095d08be8b8e74b116","ENTITY_TWITTER_USER_7ad691416f07b88a1d4bf6728299b0c7"};
        List<String> toRowkids = Arrays.asList(toRowks);
        List<GraphRelation> res = new ArrayList<>();

        Iterable<Vertex> vertices = defaultGraph.getVertices(Arrays.asList(rowks), rootAuth);
        vertices.forEach(vertex -> {
            Iterable<EdgeInfo> edgeInfos = vertex.getEdgeInfos(Direction.BOTH, rootAuth);
            edgeInfos.forEach(x->{
                if(toRowkids.contains(x.getVertexId())){
                    res.add(new GraphRelation(vertex,
                            defaultGraph.getEdge(x.getEdgeId(),rootAuth),
                            defaultGraph.getVertex(x.getVertexId(),rootAuth)));
                }
            });
        });
    }

}
