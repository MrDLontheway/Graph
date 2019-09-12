package com.wxscistor.pojo.vertexium;

import org.vertexium.Visibility;
import org.vertexium.query.Predicate;

import java.io.Serializable;

/**
 * @Author: dl
 * @Date: 2018/12/17 18:15
 * @Version 1.0
 */
public class GraphProperty implements Serializable{
    public String key;
    public Object value;
    public Visibility visibility;
    public Predicate predicate;

    public GraphProperty(){
        super();
    }

    public GraphProperty(String key, Object value, Visibility visibility) {
        this.key = key;
        this.value = value;
        this.visibility = visibility;
    }

    public GraphProperty(String key, Object value, Predicate predicate) {
        this.key = key;
        this.value = value;
        this.predicate = predicate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }
}
