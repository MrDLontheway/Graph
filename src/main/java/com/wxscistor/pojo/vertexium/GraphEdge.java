package com.wxscistor.pojo.vertexium;

import org.vertexium.Visibility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: dl
 * @Date: 2018/12/6 16:20
 * @Version 1.0
 */
public class GraphEdge implements Serializable{
    public String rowKey;  //主键
    public String fromKey;  //出发点
    public String toKey; //目标点
    public String label; //边关系类型
    public Visibility visibility; //可视化权限
    public Map<String,GraphProperty> properties = new HashMap<>();

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }

    public String getToKey() {
        return toKey;
    }

    public void setToKey(String toKey) {
        this.toKey = toKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Map<String, GraphProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, GraphProperty> properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromKey,toKey,label,visibility);
    }

    @Override
    public boolean equals(Object obj) {
        GraphEdge ge = (GraphEdge) obj;
        if(this.fromKey.equals(ge.fromKey)&&this.toKey.equals(ge.toKey)&&this.label.equals(ge.label)&&this.visibility==ge.visibility){
            return true;
        }else {
            return false;
        }
    }
}
