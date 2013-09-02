package com.paic.feature.dsl;

import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.classpath.ResourceFilter;
import groovy.util.ObjectGraphBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-29
 * Time: PM2:35
 * To change this template use File | Settings | File Templates.
 */
public class ScanerClassNameResolver implements ObjectGraphBuilder.ClassNameResolver {
    String rootPackage;

    public ScanerClassNameResolver(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    @Override
    public String resolveClassname( final String classname) {
        String newClass;
        if (classname.length() == 1) {
            newClass= classname.toUpperCase();
        }
        newClass= classname.substring(0, 1)
                .toUpperCase() + classname.substring(1);
        ClassPathFactory factory = new ClassPathFactory();
        ClassPath classPath = factory.createFromJVM();
        final List<String> pName = new ArrayList<String>();
        String[] re = classPath.findResources(rootPackage, new ResourceFilter() {
            @Override
            public boolean match(String packageName, String resourceName) {
                boolean match = resourceName.equalsIgnoreCase(classname + ".class");
                if (match){
                    pName.add(packageName);
                }
                return match;
            }
        });
        if (re.length==1){
            String name = (pName.get(pName.size() - 1) + "/" + newClass).replace("/", ".");
            return name;
        }else if(re.length>1){
            throw new IllegalStateException("resolve class name failed, duplicated - "+classname);
        }
        throw new IllegalStateException("resolve class name failed - "+classname);
    }
}
