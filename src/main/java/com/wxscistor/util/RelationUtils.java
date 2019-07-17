package com.wxscistor.util;

import com.wxscistor.pojo.vertexium.GraphProperty;
import com.wxscistor.pojo.vertexium.GraphRelation;
import org.vertexium.Direction;
import org.vertexium.Edge;
import org.vertexium.EdgeInfo;
import org.vertexium.Vertex;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;
import org.vertexium.query.Query;
import org.vertexium.query.QueryResultsIterable;
import org.vertexium.query.SortDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.wxscistor.config.VertexiumConfig.rootAuth;

public class RelationUtils {
    private AccumuloGraph graph;
    private AccumuloAuthorizations auth;
    private Direction d = Direction.BOTH;
    private String sortPro;
    private SortDirection sortDirection;

    private int skip;
    private long limit;

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setSortPro(String sortPro) {
        this.sortPro = sortPro;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public RelationUtils(AccumuloGraph graph, AccumuloAuthorizations auth) {
        this.graph = graph;
        this.auth = auth;
    }

    public Query findOtherV(String rowKey, String label) {
        return findOtherV(rowKey, label, Direction.BOTH, null);
    }

    public Query findOtherV(String rowKey, String label, Direction d) {
        return findOtherV(rowKey, label, d, null);
    }

    public Query findOtherV(String rowKey, String label, Direction d, GraphProperty relatProperty) {
        AccumuloGraph graph = this.graph;
        if (d == null) {
            d = this.d;
        }
        Vertex vertex = graph.getVertex(rowKey, auth);
        ArrayList<String> ids = new ArrayList<>();
        Iterable<String> vertexIds = vertex.getVertexIds(d, label, auth);
        vertexIds.forEach(x -> {
            ids.add(x);
        });
        Query has = graph.query(auth).hasId(ids);
        if (relatProperty != null) {
            has.has(relatProperty.key, relatProperty.predicate, relatProperty.value);
        }
        return has;
    }

    public Query findOtherV(String[] rowKeys, String label, Direction d) {
        ArrayList<GraphProperty> gps = new ArrayList<>();
        ArrayList<GraphProperty> rps = new ArrayList<>();
        return findOtherV(rowKeys, gps, label, d, rps);
    }

    public Query findOtherV(String[] rowKeys, String label, Direction d, GraphProperty relatProperty) {
        ArrayList<GraphProperty> gps = new ArrayList<>();

        ArrayList<GraphProperty> rps = new ArrayList<>();
        rps.add(relatProperty);
        return findOtherV(rowKeys, gps, label, d, rps);
    }

    public Query findOtherV(String[] rowKeys, GraphProperty graphProperty, String label, Direction d, GraphProperty relatProperty) {
        ArrayList<GraphProperty> gps = new ArrayList<>();
        gps.add(graphProperty);

        ArrayList<GraphProperty> rps = new ArrayList<>();
        rps.add(relatProperty);
        return findOtherV(rowKeys, gps, label, d, rps);
    }

    public Query findOtherV(String[] rowKeys, List<GraphProperty> gps, String label, Direction d, List<GraphProperty> rps) {
        AccumuloGraph graph = this.graph;
        if (d == null) {
            d = this.d;
        }
        Query has = graph.query(auth).hasId(Arrays.asList(rowKeys));
        for (GraphProperty graphProperty : gps) {
            has.has(graphProperty.key, graphProperty.predicate, graphProperty.value);
        }
        ArrayList<String> ids = new ArrayList<>();
        for (Vertex x : has.vertices()) {
            Iterable<String> vertexIds = x.getVertexIds(d, label, auth);
            vertexIds.forEach(y -> {
                ids.add(y);
            });
        }
        Query has1 = graph.query(auth).hasId(ids);
        for (GraphProperty graphProperty : rps) {
            has1.has(graphProperty.key, graphProperty.predicate, graphProperty.value);
        }
        if (skip != 0 && limit != 0) {
            has1.skip(skip);
            has1.limit(limit);
        }
        if (sortPro != null && sortDirection != null) {
            has1.sort(sortPro, sortDirection);
        }
        return has1;
    }

    //===============获取点 边 点 relation
    public List<GraphRelation> findRelation(String[] rowKeys, List<GraphProperty> gps, String[] labels, Direction d, List<GraphProperty> eps, List<GraphProperty> rps) {
        AccumuloGraph graph = this.graph;
        if (d == null) {
            d = this.d;
        }
        List<GraphRelation> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> edgeids = new ArrayList<>();
        //存放所有需要索引点 缓存
        HashMap<String, Vertex> vers = new HashMap<>();
        Query query = graph.query(auth).hasId(rowKeys);
        gps.forEach(graphProperty -> query.has(graphProperty.key, graphProperty.predicate, graphProperty.value));

        QueryResultsIterable<Vertex> vertices = query.vertices();
        for (Vertex vertex : vertices) {
            Iterable<String> edgeIds = vertex.getEdgeIds(d, labels, auth);
            edgeIds.forEach(z -> edgeids.add(z));
            Iterable<String> vertexIds = vertex.getVertexIds(d, labels, auth);
            vertexIds.forEach(y -> ids.add(y));
            vers.put(vertex.getId(), vertex);
        }

        //relation 关系点
        Query query1 = graph.query(auth).hasId(ids);
        rps.forEach(graphProperty -> query1.has(graphProperty.key, graphProperty.predicate, graphProperty.value));
        query1.vertices().forEach(x -> vers.put(x.getId(), x));

        //过滤边
        Query query2 = graph.query(auth)
                .hasId(edgeids);
        if (labels != null)
            query2.hasEdgeLabel(labels);
        eps.forEach(graphProperty -> query2.has(graphProperty.key, graphProperty.predicate, graphProperty.value));
        query2.edges().forEach(e -> {
            String outVertexId = e.getVertexId(Direction.OUT);
            String inVertexId = e.getVertexId(Direction.IN);
            Vertex out = vers.get(outVertexId);
            Vertex in = vers.get(inVertexId);
            if (out != null && in != null) {
                result.add(new GraphRelation(out, e, in));
            }
        });
        return result;
    }

    //===============获取点 边 点 relation
    public List<GraphRelation> findRelation(String[] rowKeys, List<GraphProperty> gps, String[] labels, Direction d, List<GraphProperty> eps, String[] toKeys, List<GraphProperty> rps) {
        AccumuloGraph graph = this.graph;
        if (d == null) {
            d = this.d;
        }
        List<GraphRelation> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> edgeids = new ArrayList<>();
        //存放所有需要索引点 缓存
        HashMap<String, Vertex> vers = new HashMap<>();
        Query query = graph.query(auth).hasId(rowKeys);
        gps.forEach(graphProperty -> query.has(graphProperty.key, graphProperty.predicate, graphProperty.value));

        QueryResultsIterable<Vertex> vertices = query.vertices();
        for (Vertex vertex : vertices) {
            Iterable<String> edgeIds = vertex.getEdgeIds(d, labels, auth);
            edgeIds.forEach(z -> edgeids.add(z));
            Iterable<String> vertexIds = vertex.getVertexIds(d, labels, auth);
            vertexIds.forEach(y -> ids.add(y));
            vers.put(vertex.getId(), vertex);
        }

        //relation 关系点
        Query query1 = graph.query(auth).hasId(toKeys);
        rps.forEach(graphProperty -> query1.has(graphProperty.key, graphProperty.predicate, graphProperty.value));
        query1.vertices().forEach(x -> vers.put(x.getId(), x));

        //过滤边
        Query query2 = graph.query(auth)
                .hasId(edgeids);
        if (labels != null)
            query2.hasEdgeLabel(labels);

        eps.forEach(graphProperty -> query2.has(graphProperty.key, graphProperty.predicate, graphProperty.value));
        query2.edges().forEach(e -> {
            String outVertexId = e.getVertexId(Direction.OUT);
            String inVertexId = e.getVertexId(Direction.IN);
            Vertex out = vers.get(outVertexId);
            Vertex in = vers.get(inVertexId);
            if (out != null && in != null) {
                result.add(new GraphRelation(out, e, in));
            }
        });
        return result;
    }

    public List<GraphRelation> findOtherVRelation(String rowKey, String label, Direction d, GraphProperty relatProperty) {
        AccumuloGraph graph = this.graph;
        List<GraphRelation> result = new ArrayList<>();
        if (d == null) {
            d = this.d;
        }
        Vertex vertex = graph.getVertex(rowKey, auth);
        //存放所有需要索引点 缓存
        HashMap<String, Vertex> vers = new HashMap<>();
        vers.put(vertex.getId(), vertex);
        Iterable<String> vertexIds = vertex.getVertexIds(d, label, auth);
        Query has = graph.query(auth).hasId(vertexIds);
        if (relatProperty != null) {
            has.has(relatProperty.key, relatProperty.predicate, relatProperty.value);
        }
        has.vertices().forEach(x -> vers.put(x.getId(), x));
        Iterable<Edge> edges = vertex.getEdges(d, label, auth);
        edges.forEach(e -> {
            String outVertexId = e.getVertexId(Direction.OUT);
            String inVertexId = e.getVertexId(Direction.IN);
            Vertex out = vers.get(outVertexId);
            Vertex in = vers.get(inVertexId);
            if (out != null && in != null) {
                result.add(new GraphRelation(out, e, in));
            }
        });
        return result;
    }

    public List<GraphRelation> findRelation(String[] rowKeys, String[] labels, Direction d, String[] toKeys) {
        AccumuloGraph graph = this.graph;
        if (d == null) {
            d = this.d;
        }
        List<GraphRelation> result = new ArrayList<>();
        List<String> toRowkids = Arrays.asList(toKeys);

        Iterable<Vertex> vertices = graph.getVertices(Arrays.asList(rowKeys), auth);
        for (Vertex vertex1 : vertices) {
            Iterable<EdgeInfo> edgeInfos = vertex1.getEdgeInfos(d, labels, auth);
            edgeInfos.forEach(x -> {
                if (toRowkids.contains(x.getVertexId())) {
                    Edge edge = graph.getEdge(x.getEdgeId(), auth);
                    Vertex vertex = graph.getVertex(x.getVertexId(), auth);
                    if (vertex != null && edge != null) {
                        result.add(new GraphRelation(vertex1,
                                edge,
                                vertex));
                    }
                }
            });
        }
        return result;
    }


}
