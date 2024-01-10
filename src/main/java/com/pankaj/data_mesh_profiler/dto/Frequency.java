package com.pankaj.data_mesh_profiler.dto;


public class Frequency {

    private Object value;
    private long count;

    // getters and setters

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
