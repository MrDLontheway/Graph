package com.wxscistor.pojo.vertexium;

import org.elasticsearch.index.query.QueryBuilder;
import org.vertexium.query.Compare;
import org.vertexium.query.Predicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图库基础查询 help
 * @Author: dl
 * @Date: 2018/12/6 14:54
 * @Version 1.0
 */
public class VertexiumQuery implements Serializable {
    public String keyword;
    public boolean isSimilar;
    public String[] similarFields;
    public boolean isSimilar() {
        return isSimilar;
    }
    public List<QueryBuilder> qbs = new ArrayList<>();
    public List<QueryBuilder> sholdQueryBuilders = new ArrayList<>();
    public void setSimilar(boolean similar) {
        isSimilar = similar;
    }

    public String[] getSimilarFields() {
        return similarFields;
    }

    public void setSimilarFields(String[] similarFields) {
        this.similarFields = similarFields;
    }

    public VertexiumQuery(String keyword,String... auths){
        this.keyword = keyword;
        this.auths = auths;
        this.isSimilar = false;
    }

    public VertexiumQuery(String keyword,String[] similarFields,String... auths){
        this.keyword = keyword;
        this.auths = auths;
        this.similarFields = similarFields;
        this.isSimilar = true;
    }

    public VertexiumQuery(String... auths){
        this.auths = auths;
    }
    //属性
    public Map<String,Object> properties = new HashMap<>();
    public Map<String,Predicate> propertiesPrediacte = new HashMap<>();
    //边标签包含
    public List<String> labels = new ArrayList<>();
    //ID  包含

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Predicate> getPropertiesPrediacte() {
        return propertiesPrediacte;
    }

    public void setPropertiesPrediacte(Map<String, Predicate> propertiesPrediacte) {
        this.propertiesPrediacte = propertiesPrediacte;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getVertexIds() {
        return VertexIds;
    }

    public void setVertexIds(List<String> vertexIds) {
        VertexIds = vertexIds;
    }

    public List<String> getEdgeIds() {
        return EdgeIds;
    }

    public void setEdgeIds(List<String> edgeIds) {
        EdgeIds = edgeIds;
    }

    public String[] getAuths() {
        return auths;
    }

    public void setAuths(String[] auths) {
        this.auths = auths;
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean page) {
        isPage = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<String> VertexIds = new ArrayList<>();
    //ID  包含
    public List<String> EdgeIds = new ArrayList<>();

    public String[] auths = new String[]{};

    public List<Range> ranges = new ArrayList();

    public boolean isPage = false;

    public int pageSize = 10;

    public int pageIndex = 0;

    public VertexiumQuery has(String key,Object value){
        has(key,value,Compare.EQUAL);
        return this;
    }

    public VertexiumQuery has(String key,Object value,Predicate predicate){
        this.properties.put(key,value);
        this.propertiesPrediacte.put(key,predicate);
        return this;
    }

    public VertexiumQuery hasNot(String key,Object value){
        this.properties.put(key,value);
        this.propertiesPrediacte.put(key,Compare.NOT_EQUAL);
        return this;
    }

    public VertexiumQuery hasVertexId(String... ids){
        for (String id:
        ids) {
            VertexIds.add(id);
        }
        return this;
    }

    public VertexiumQuery hasEdgeId(String... ids){
        for (String id:
                ids) {
            EdgeIds.add(id);
        }
        return this;
    }

    public VertexiumQuery range(String key,Object startValue,Object endValue,boolean includStart,boolean includEnd){
        this.ranges.add(new Range(key,startValue,endValue,includStart,includEnd));
        return this;
    }

    public VertexiumQuery range(String key,Object startValue,Object endValue){
        this.ranges.add(new Range(key,startValue,endValue,true,true));
        return this;
    }

    public VertexiumQuery page(int pageSize,int pageIndex){
        this.isPage = true;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        return this;
    }

    public VertexiumQuery hasLabels(String... labs){
        for (String id:
                labs) {
            labels.add(id);
        }
        return this;
    }

    public VertexiumQuery addQueryBuilder(QueryBuilder queryBuilder){
        this.qbs.add(queryBuilder);
        return this;
    }

    public VertexiumQuery addSholdQueryBuilders(QueryBuilder queryBuilder){
        this.sholdQueryBuilders.add(queryBuilder);
        return this;
    }
}
