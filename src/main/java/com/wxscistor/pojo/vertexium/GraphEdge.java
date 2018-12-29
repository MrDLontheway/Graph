package com.wxscistor.pojo.vertexium;

import org.vertexium.Visibility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    public transient Visibility visibility; //可视化权限
    public transient Map<String,Visibility> propertiesVisibility = new HashMap<>();

    public Map<String, Visibility> getPropertiesVisibility() {
        return propertiesVisibility;
    }

    public void setPropertiesVisibility(Map<String, Visibility> propertiesVisibility) {
        this.propertiesVisibility = propertiesVisibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public GraphEdge() {
        super();
    }

    public GraphEdge(String rowKey, String fromKey, String toKey, String label) {
        this.rowKey = rowKey;
        this.fromKey = fromKey;
        this.toKey = toKey;
        this.label = label;
    }

    public Map<String,Object> properties = new HashMap<>();

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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
