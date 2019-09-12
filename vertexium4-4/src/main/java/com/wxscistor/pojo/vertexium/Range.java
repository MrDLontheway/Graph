package com.wxscistor.pojo.vertexium;

import java.io.Serializable;

/**
 * @Author: dl
 * @Date: 2018/12/6 15:48
 * @Version 1.0
 */
public class Range<T> implements Serializable {
    String key;
    T startValue;
    T endValue;
    boolean includStart = true;
    boolean includEnd = true;

    Range(String key, T startValue, T endValue, boolean includStart, boolean includEnd) {
        this.key = key;
        this.startValue = startValue;
        this.endValue = endValue;
        this.includEnd = includEnd;
        this.includStart = includStart;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getStartValue() {
        return startValue;
    }

    public void setStartValue(T startValue) {
        this.startValue = startValue;
    }

    public T getEndValue() {
        return endValue;
    }

    public void setEndValue(T endValue) {
        this.endValue = endValue;
    }

    public boolean isIncludStart() {
        return includStart;
    }

    public void setIncludStart(boolean includStart) {
        this.includStart = includStart;
    }

    public boolean isIncludEnd() {
        return includEnd;
    }

    public void setIncludEnd(boolean includEnd) {
        this.includEnd = includEnd;
    }
}
