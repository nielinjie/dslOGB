package com.paic.feature;

import java.util.ArrayList;
import java.util.List;

public class Requirement {
	private String name;
	private String description;
    private Condition condition;
    private List<Feature> features=new ArrayList<Feature>();
    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
