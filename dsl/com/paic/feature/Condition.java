package com.paic.feature;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-28
 * Time: PM2:28
 * To change this template use File | Settings | File Templates.
 */
public class Condition {
    static private Logger logger= Logger.getLogger(Condition.class);


    private String exp;

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }


    public boolean apply(Object owner, Context context) {
        Binding bind = new Binding();
        bind.setVariable("owner", owner);
        bind.setVariable("context", context);
        //FIXME performance issue ?
        GroovyShell groovyShell = new GroovyShell(bind);
        try {
            return (Boolean) groovyShell.evaluate(exp);
        } catch (Exception e) {
            logger.warn("expCondition evaluate failed.",e);
            return false;
        }
    }}
