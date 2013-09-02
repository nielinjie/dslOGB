package com.paic.feature;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-30
 * Time: PM5:06
 * To change this template use File | Settings | File Templates.
 */
public class Entry<K,V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
