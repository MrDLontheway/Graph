package com.dl;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.pojo.vertexium.GraphProperty;
import com.wxscistor.util.RelationUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.*;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.*;

import java.util.*;

public class GraphBatch {
//    @Test
////    public void insertBatch(){
////        String[] linkType = new String[]{"friend","like","forward","dislike","post","phone","message"};
////        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
////        for (int j = 0; j < 935; j++) { //652W
////            ArrayList<ElementBuilder<Vertex>> vs = new ArrayList<>();
////            for (int i = 0; i < 100000; i++) {//1E 条点 数据
////                VertexBuilder vertexBuilder = most.prepareVertex(Visibility.EMPTY);
////                Map<String,Object> address = RandomValue.getAddress();
////                address.forEach((x,y)->{
////                    vertexBuilder.setProperty(x,y,Visibility.EMPTY);
////                });
////                vertexBuilder.setIndexHint(IndexHint.DO_NOT_INDEX);
////                vs.add(vertexBuilder);
////            }
////            most.getSearchIndex().addElements(most,most.addVertices(vs,new AccumuloAuthorizations()),new AccumuloAuthorizations());
////            most.flush();
////        }
////    }

    @Test
    public void testquery() throws AccumuloSecurityException, AccumuloException {
        //most mostvertexium
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mergevertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));
        List<String> ids = new ArrayList<>();
        Iterable<Vertex> vertices = most.getVertices(root);
        most.getSearchIndex().addElements(most,vertices,root);

        Iterable<Edge> edges = most.getEdges(root);
        most.getSearchIndex().addElements(most,edges,root);
        most.flush();
//        long vertexCount = most.getVertexCount(root);
//        Iterable<Vertex> verticesWithPrefix = most.getVerticesInRange(new Range("0","5499627842"), root);
//        Vertex next = verticesWithPrefix.iterator().next();
//        most.getSearchIndex().addElement(most,next,root);
//        verticesWithPrefix.forEach(x->{
//            ids.add(x.getId());
//        });
//        System.out.println(ids.size());
    }

    @Test
    public void testqueryone() throws AccumuloSecurityException, AccumuloException {
        //most mostvertexium
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("batchvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));
        List<String> ids = new ArrayList<>();
        Iterable<Vertex> vertices = most.getVertices(root);
        vertices.forEach(x->{
            most.getSearchIndex().addElement(most,x,root);
        });
        most.flush();
//        long vertexCount = most.getVertexCount(root);
//        Iterable<Vertex> verticesWithPrefix = most.getVerticesInRange(new Range("0","5499627842"), root);
//        Vertex next = verticesWithPrefix.iterator().next();
//        most.getSearchIndex().addElement(most,next,root);
//        verticesWithPrefix.forEach(x->{
//            ids.add(x.getId());
//        });
//        System.out.println(ids.size());
    }

    @Test
    public void testquery2() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));

        Aggregation aggregation = new TermsAggregation("countSex","sex");

        Query has = most.query(root)
//                .range("age",10,15)
                .sort("birthday",SortDirection.DESCENDING)
                .skip(10)
                .limit(10)
                .addAggregation(aggregation);


        QueryResultsIterable<Vertex> vertices = has.vertices();
        TermsResult countSex = vertices.getAggregationResult("countSex", TermsResult.class);
        long totalHits = vertices.getTotalHits();
        vertices.forEach(x->{
            System.out.println(x);
        });
//        vertices.forEach(x->{
//            System.out.println(x);
//        });
        System.out.println(totalHits);
    }

    @Test
    public void addedge() throws AccumuloSecurityException, AccumuloException {
        String[] linkType = new String[]{"friend","like","forward","dislike","post","phone","message"};
        Random r = new Random();
        String[] ids = new String[]{
                "549971081616687107",
                "549971081616687110",
                "549971081616687112",
                "549971081616687115",
                "549971081616687121",
                "549971081616687126",
                "549971081616687128",
                "549971081616687129",
                "549971081616687131",
                "549971081616687137",
                "549971081616687142"
        };
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));
        /**
         * 549971081612492943
         *
         * 549971081616687107
         * 549971081616687110
         * 549971081616687112
         * 549971081616687115
         * 549971081616687121
         * 549971081616687126
         * 549971081616687128
         * 549971081616687129
         * 549971081616687131
         * 549971081616687137
         * 549971081616687142
         *
         *
         *
         * 549962323511476246
         *
         * 549960718196146222
         */
        Vertex vertex = most.getVertex("549971081612492943", root);

        ArrayList<String> dds = new ArrayList<>();
        Query query = most.query(root)
                .hasId("549962323511476246", "549971081612492943");
        query.vertices().forEach(x->{
//            Iterable<String> vertexIds = x.getVertexIds(Direction.BOTH, "dislike",root);
            Iterable<String> vertexIds = x.getVertexIds(Direction.BOTH,root);
            vertexIds.forEach(y->{
                dds.add(y);
            });
        });

        Query has = most.query(root)
                .hasId(dds);
        long totalHits = has.vertices().getTotalHits();
//        for (String id : ids) {
//            Edge edge = most.addEdge("549971081612492943", id, linkType[r.nextInt(linkType.length)], Visibility.EMPTY, root);
//            System.out.println(edge);
//        }
//        most.flush();
        System.out.println(vertex);
        System.out.println(totalHits);

    }

    @Test
    public void relation() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));

        RelationUtils relationUtils = new RelationUtils(most, root);
        Query otherV = relationUtils.findOtherV("549971081616687107", "friend", Direction.BOTH, new GraphProperty("age", 10, Compare.LESS_THAN));
        long totalHits = otherV.vertices().getTotalHits();
        System.out.println(totalHits);
    }
}
