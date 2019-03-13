package com.wxscistor.util;

import com.wxscistor.config.VertexiumConfig;
import com.wxscistor.pojo.vertexium.GraphProperty;
import com.wxscistor.pojo.vertexium.GraphRelation;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.junit.Test;
import org.vertexium.Direction;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.Compare;
import org.vertexium.query.Query;

import java.util.List;

import static org.junit.Assert.*;

public class RelationUtilsTest {

    @Test
    public void findOtherV() {
    }

    @Test
    public void findOtherV1() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));

        RelationUtils relationUtils = new RelationUtils(most, root);
//        Query otherV = relationUtils.findOtherV("549971081616687107", "friend", Direction.BOTH, new GraphProperty("age", 30, Compare.GREATER_THAN_EQUAL));
        Query otherV = relationUtils.findOtherV("549971081616687107", null, Direction.BOTH, null);
        long totalHits = otherV.vertices().getTotalHits();
        System.out.println(totalHits);
    }

    @Test
    public void findOtherV2() {
    }

    @Test
    public void findOtherV3() {
    }

    @Test
    public void findOtherV4() {
    }

    @Test
    public void findOtherV5() {
    }

    @Test
    public void findOtherV6() {
    }

    @Test
    public void findRelation() {
    }

    @Test
    public void findOtherVRelation() throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph most = VertexiumConfig.createAccumuloGraph("mostvertexium");
        AccumuloAuthorizations root = new AccumuloAuthorizations(most.getConnector().securityOperations().getUserAuthorizations("root").toString().split(","));

        RelationUtils relationUtils = new RelationUtils(most, root);
//        List<GraphRelation> otherVRelation = relationUtils.findOtherVRelation("549971081616687107", "friend", Direction.BOTH, new GraphProperty("age", 30, Compare.GREATER_THAN_EQUAL));
        List<GraphRelation> otherVRelation = relationUtils.findOtherVRelation("549971081616687107", "dislike", Direction.BOTH, null);
        System.out.println(otherVRelation.size());
    }
}