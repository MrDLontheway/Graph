package com.wxscistor.pojo.vertexium;

import org.vertexium.Visibility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: dl
 * @Date: 2018/12/6 16:17
 * @Version 1.0
 */
public class GraphVertex implements Serializable{
    public String rowKey;
    public Visibility visibility; //可视化权限
    public Map<String,GraphProperty> properties = new HashMap<>();

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
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
        return Objects.hash(rowKey,visibility);
    }

    @Override
    public boolean equals(Object obj) {
        GraphVertex gv = (GraphVertex) obj;
        if(this.rowKey.equals(gv.rowKey)&&this.visibility==gv.visibility){
            return true;
        }else {
            return false;
        }
    }
}
