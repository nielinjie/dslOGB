package com.paic.feature.dsl;

import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import groovy.util.ObjectGraphBuilder;
import org.apache.log4j.Logger;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: nielinjie
 * Date: 13-8-28
 * Time: AM10:57
 * To change this template use File | Settings | File Templates.
 */
public class FeatureRelationNameResolver implements ObjectGraphBuilder.RelationNameResolver {
    private static Logger logger = Logger.getLogger(FeatureRelationNameResolver.class);
    private static final Pattern PLURAL_IES_PATTERN = Pattern.compile(".*[^aeiouy]y", Pattern.CASE_INSENSITIVE);

    private Class toClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return toClass(((ParameterizedType) type).getRawType());
        }
        throw new IllegalStateException("not a class? - " + type);
    }

    @Override
    public String resolveChildRelationName(String parentName, Object parent, String childName, Object child) {
        if(childName.startsWith("$"))
            return "";
        if (childName.contains("$")) {
            String[] parts = childName.split("\\$");
            if (parts.length != 2) {
                throw new IllegalStateException("$ hit don't understand - " + childName);
            } else {
                logger.debug("$ hit found, forward - '" + childName + "'");
                return resolveChildRelationName(parentName, parent, parts[0], child);
            }
        }
        MetaProperty metaProperty;
        //1. for collection name
        boolean matchesIESRule = PLURAL_IES_PATTERN.matcher(childName).matches();
        String childNamePlural = matchesIESRule ? childName.substring(0, childName.length() - 1) + "ies" : childName + "s";
        MetaClass parentMetaClass = InvokerHelper.getMetaClass(parent);
        metaProperty = parentMetaClass
                .hasProperty(parent, childNamePlural);
        if (metaProperty != null) {
            logger.debug("resolved relation '" + childName + "' success by name plural.");
            return childNamePlural;
        }
        //2. match Name
        metaProperty = parentMetaClass
                .hasProperty(parent, childName);
        if (metaProperty != null) {
            logger.debug("resolved relation '" + childName + "' success by name.");
            return childName;
        }

        //3. guess by type
        List<MetaProperty> byType = new ArrayList<MetaProperty>();
        for (MetaProperty property : parentMetaClass.getProperties()) {
            if (property.getType().isAssignableFrom(child.getClass())) {
                byType.add(property);
            } else {
                if (Collection.class.isAssignableFrom(property.getType())) {
                    try {
                        Field field = parent.getClass().getDeclaredField(property.getName());
                        Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                        if (types.length == 1) {
                            if (toClass(types[0]).isAssignableFrom(child.getClass())) {
                                byType.add(property);
                            }
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (byType.size() == 1) {
            logger.debug("resolved relation '" + childName + "' success by type guess.");
            return byType.get(0).getName();
        } else if (byType.size() > 1) {
            MetaProperty re = findInClasses(byType);
            if (re == null)
                logger.debug("guess by type failed, duplicated - " + childName + ":" + child.getClass().getName());
            else
                return re.getName();
        }

        //4. guess by name
        List<MetaProperty> byName = new ArrayList<MetaProperty>();
        for (MetaProperty property : parentMetaClass.getProperties()) {
            //TODO think about camel case.
            if (childName.toUpperCase().contains(property.getName().toUpperCase())) {
                byName.add(property);
            }
        }
        if (byName.size() == 1) {
            logger.debug("resolved relation '" + childName + "' success by name guess.");
            return byName.get(0).getName();
        } else if (byName.size() > 1) {
            throw new IllegalStateException("guess by name failed, duplicated - " + childName);
        }
        logger.warn("no property find for - " + childName);
        return "";

    }

    private MetaProperty findInClasses(List<MetaProperty> byType) {
        List<MetaProperty> re=new ArrayList<MetaProperty>();
        for(MetaProperty mp:byType){
            Class clazz=mp.getType();
            boolean all=true;
            for(MetaProperty otherP:byType){
                all=all && otherP.getType().isAssignableFrom(clazz);
            }
            if(all) re.add(mp);
        }
        if(re.size()==1)
            return re.get(0);
        else if (re.size()>1){
            logger.warn("duplicated property find - "+re);
            return re.get(0);
        }else
            return null;  //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public String resolveParentRelationName(String parentName, Object parent, String childName, Object child) {
        return parentName;
    }
}
