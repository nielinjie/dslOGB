package com.paic.feature;

import com.paic.feature.dsl.Dsl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-28
 * Time: PM12:03
 * To change this template use File | Settings | File Templates.
 */
public class FeatureManager {
    private Dsl dsl;

    public Dsl getDsl() {
        return dsl;
    }

    public void setDsl(Dsl dsl) {
        this.dsl = dsl;
    }

    public List<Feature> getFeatures(Object owner, Context context) {
        List<Feature> re = new ArrayList<Feature>();
        for (Requirement requirement : dsl.getRequirements()) {
            if (requirement.getCondition().apply(owner, context)) {
                re.addAll(requirement.getFeatures());
            }
        }
        return re;
    }
}
