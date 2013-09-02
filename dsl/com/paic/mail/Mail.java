package com.paic.mail;

import com.paic.feature.Feature;
import com.paic.feature.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-30
 * Time: PM5:51
 * To change this template use File | Settings | File Templates.
 */
public class Mail implements Feature{
    private List<Entry<String,String>> handleMap=new ArrayList<Entry<String, String>>();

    public List<Entry<String, String>> getHandleMap() {
        return handleMap;
    }

    public void setHandleMap(List<Entry<String, String>> handleMap) {
        this.handleMap = handleMap;
    }
}
