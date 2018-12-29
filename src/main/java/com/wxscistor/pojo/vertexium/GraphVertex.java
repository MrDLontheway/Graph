package com.wxscistor.pojo.vertexium;

import org.vertexium.Visibility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dl
 * @Date: 2018/12/6 16:17
 * @Version 1.0
 */
public class GraphVertex implements Serializable{
    public String rowKey;
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

    public GraphVertex() {
        super();
    }

    public GraphVertex(String rowKey) {
        this.rowKey = rowKey;
    }

    public Map<String,Object> properties = new HashMap<>();

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
