package com.dl.Relat;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.pojo.vertexium.GraphRelation;
import com.wxscistor.util.AuthUtils;
import com.wxscistor.util.RelationUtils;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.Direction;
import org.vertexium.Edge;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelationTest {

    AccumuloGraph defaultGraph = VertexiumConfig.defaultGraph;
    AccumuloAuthorizations rootAuth = AuthUtils.getRootAuth(defaultGraph);
    String[] rowks = new String[]{"ENTITY_TWITTER_USER_61d2e0d98c299f68540ab2db8731429e"};
    String[] toRowks = new String[]{"EVENT_TWITTER_POST_af5e0d2a75ac19095d08be8b8e74b116","ENTITY_TWITTER_USER_82db2231b98517f0f7c2f0e95a83efb1","ENTITY_FACEBOOK_USER_a044d6923febf79c7c2fba097cad84d9","ENTITY_TWITTER_USER_65b7cfbfa590183364959d456d399a5c","EVENT_WEIBO_POST_b2eb635c53ccc988093249a9f322c1d9","ENTITY_IMPORTANT_PERSON_c034d8f6e44158d2f73fb70ebf9392ac","ENTITY_ZZZZ_60ae7e134c0027b06b7711e871017be7","DOCUMENT_DOCUMENT_01_f7ecc68a37774653a77cb69cbce054e2","MEDIA_MEDIA_01_e46b67b87cb846db9c7e357ac8cb45eb","EVENT_TWITTER_POST_af5e0d2a75ac19095d08be8b8e74b116","ENTITY_TWITTER_USER_7ad691416f07b88a1d4bf6728299b0c7"};

    public RelationTest() throws AccumuloSecurityException, AccumuloException {
    }

    @Test
    public void edgeconfirm() throws AccumuloSecurityException, AccumuloException {
        Iterable<Vertex> vertices = defaultGraph.getVertices(rootAuth);
        vertices.forEach(x->{
            System.out.println(x);
        });
//        RelationUtils relationUtils = new RelationUtils(defaultGraph, rootAuth);
//        List<GraphRelation> res = relationUtils.findRelation(rowks, null, null, toRowks);
//        List<String> toRowkids = Arrays.asList(toRowks);
//        List<GraphRelation> res = new ArrayList<>();
//
//        String[] labels = null;
//
//        Iterable<Vertex> vertices = defaultGraph.getVertices(Arrays.asList(rowks), rootAuth);
//        vertices.forEach(vertex -> {
//            Iterable<EdgeInfo> edgeInfos = vertex.getEdgeInfos(Direction.BOTH,rootAuth);
//            edgeInfos.forEach(x->{
//                if(toRowkids.contains(x.getVertexId())){
//                    res.add(new GraphRelation(vertex,
//                            defaultGraph.getEdge(x.getEdgeId(),rootAuth),
//                            defaultGraph.getVertex(x.getVertexId(),rootAuth)));
//                }
//            });
//        });
//        System.out.println(res.size());
    }

    @Test
    public void edgeconfirmES(){
        RelationUtils relationUtils = new RelationUtils(defaultGraph, rootAuth);
        List<GraphRelation> res = relationUtils.findRelation(rowks, new ArrayList<>(), null, null,new ArrayList<>(),toRowks,new ArrayList<>());
        System.out.println(res.size());
//
    }


    @Test
    public void edgeconfirm3() throws AccumuloSecurityException, AccumuloException {
        List<String> toRowkids = Arrays.asList(toRowks);
        List<GraphRelation> res = new ArrayList<>();

        Iterable<Vertex> vertices = defaultGraph.getVertices(Arrays.asList(rowks), rootAuth);
        vertices.forEach(vertex -> {
            Iterable<Edge> edgeInfos = vertex.getEdges(Direction.BOTH, rootAuth);
            edgeInfos.forEach(x->{
                if(toRowkids.contains(x.getOtherVertexId(vertex.getId()))){
                    res.add(new GraphRelation(vertex,
                            x,
                            x.getOtherVertex(vertex.getId(),rootAuth)));
                }
            });
        });
        System.out.println(res.size());
    }


    @Test
    public void tt1(){
        Vertex vertex = defaultGraph.getVertex("ENTITY_TWITTER_USER_61d2e0d98c299f68540ab2db8731429e",rootAuth);
//        defaultGraph.deleteEdge("3aecaaf8e59b7076a57724b4c72ecb7c",rootAuth);
//        defaultGraph.flush();
        System.out.println(vertex);
    }
}
