package com.paic.show;

import com.paic.feature.Feature;
import com.paic.feature.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-30
 * Time: PM5:46
 * To change this template use File | Settings | File Templates.
 */
public class Show implements Feature{
    private List<Flag> flags= new ArrayList<Flag>();

    public List<Flag> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }
}
