package com.paic.dynamicProperty;

import com.paic.feature.Feature;
import com.paic.feature.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-30
 * Time: PM2:48
 * To change this template use File | Settings | File Templates.
 */
public class DynamicProperty implements Feature {
    private String name;
    private String cName;
    private Class type;
    private Validator validator;
    private Editor editor;
    private Integer maxLength;
    private Integer minLength;
    private Boolean isRequired;
    private Object defaultValue;
    private List<Entry<String, Renderer>> renderMap = new ArrayList<Entry<String, Renderer>>();
    private ActiveEvent activeEvent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }


    public List<Entry<String, Renderer>> getRenderMap() {
        return renderMap;
    }

    public void setRenderMap(List<Entry<String, Renderer>> renderMap) {
        this.renderMap = renderMap;
    }

    public ActiveEvent getActiveEvent() {
        return activeEvent;
    }

    public void setActiveEvent(ActiveEvent activeEvent) {
        this.activeEvent = activeEvent;
    }
}
