package com.paic.feature.dsl;

import java.util.ArrayList;
import java.util.List;

import com.paic.feature.Condition;
import com.paic.feature.Requirement;


import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.ObjectGraphBuilder;

public class Dsl {

    private DslSource source;
    private List<Requirement> requirements=new ArrayList<Requirement>();
    public DslSource getSource() {
        return source;
    }

    public void setSource(DslSource source) {
        this.source = source;
    }
    private List<Class<?>> registeredClasses;

    public List<Class<?>> getRegisteredClasses() {
        return registeredClasses;
    }

    public void setRegisteredClasses(List<Class<?>> registeredClasses) {
        this.registeredClasses = registeredClasses;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }


    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public void setup() {
		ObjectGraphBuilder dsl= new ObjectGraphBuilder();
        FeatureClassNameResolver classNameResolver = new FeatureClassNameResolver(dsl, "com/paic/feature", registeredClasses,"com/paic");
        classNameResolver.setInternalRegisteredClasses(new Class<?>[]{Condition.class,Requirement.class});
        dsl.setClassNameResolver(classNameResolver);
        dsl.setRelationNameResolver(new FeatureRelationNameResolver());
		Binding bind =new Binding();
		bind.setVariable("dsl", dsl);
		GroovyShell groovyShell=new GroovyShell(bind);
            requirements.clear();
		for(String s:source.getDsls()){
            Requirement requirement = (Requirement) groovyShell.evaluate(s);
            requirements.add(requirement);
        }
	}
}
