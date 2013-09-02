package com.paic.feature.dsl;

import org.apache.log4j.Logger;
import groovy.util.ObjectGraphBuilder;
import groovy.util.ObjectGraphBuilder.ClassNameResolver;
import groovy.util.ObjectGraphBuilder.ReflectionClassNameResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class FeatureClassNameResolver extends ReflectionClassNameResolver
        implements ClassNameResolver {
    private static Logger logger=Logger.getLogger(FeatureClassNameResolver.class);
    private ObjectGraphBuilder objectGraphBuilder;
    private String root;
    ScanerClassNameResolver scanerClassNameResolver;

    private static final Pattern PLURAL_IES_PATTERN = Pattern.compile(".*[^aeiouy]y", Pattern.CASE_INSENSITIVE);

    public FeatureClassNameResolver(ObjectGraphBuilder objectGraphBuilder,
                                    String root, List<Class<?>> registeredClasses,String rootPackage) {
        objectGraphBuilder.super(root);
        this.root = root;
        this.objectGraphBuilder = objectGraphBuilder;
        this.registeredClasses = registeredClasses;
        scanerClassNameResolver = new ScanerClassNameResolver(rootPackage);
    }

    private List<Class<?>> registeredClasses;
    private Class<?>[] internalRegisteredClasses;

    public Class<?>[] getInternalRegisteredClasses() {
        return internalRegisteredClasses;
    }

    public void setInternalRegisteredClasses(Class<?>[] internalRegisteredClasses) {
        this.internalRegisteredClasses = internalRegisteredClasses;
    }

    public List<Class<?>> getRegisteredClasses() {
        return registeredClasses;
    }

    public void setRegisteredClasses(List<Class<?>> registeredClasses) {
        this.registeredClasses = registeredClasses;
    }

    private static String makeClassName(String root, String name) {
        return root + "." + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public String resolveClassname(String classname) {
        if(classname.startsWith("$")){
            return resolveClassname(classname.substring(1));
        }
        if(classname.contains("$")){
            String[] parts = classname.split("\\$");
            if(parts.length!=2){
                throw new IllegalStateException("$ hit don't understand - "+classname);
            }else{
                logger.debug("$ hit found, forward - '"+classname+"'");
                return  resolveClassname(parts[1]);
            }
        }
        for (Class clazz : internalRegisteredClasses) {
            if (clazz.getSimpleName().equalsIgnoreCase(classname)) {
                logger.debug("resolved className '"+classname+"' success in internalRegisteredClasses.");
                return clazz.getName();
            }
        }
        for (Class clazz : registeredClasses) {
            if (clazz.getSimpleName().equalsIgnoreCase(classname)) {
                logger.debug("resolved className '"+classname+"' success in registeredClasses.");
                return clazz.getName();
            }
        }



        try {
            String re= scanerClassNameResolver.resolveClassname(classname);
            if (re!=null){
                logger.debug("resolved className '"+classname+"' success by scannerClassNameResolver.");
                return re;
            }
        }catch (Exception e){

        }


        Object currentNode = objectGraphBuilder.getContext().get(objectGraphBuilder.CURRENT_NODE);

        if (currentNode == null) {
            logger.debug("resolved className '"+classname+"' success by root of reflection resolver.");
            return makeClassName(root, classname);
        } else {
            Field declaredField = null;
            try {
                declaredField = currentNode.getClass().getDeclaredField(classname);
            } catch (NoSuchFieldException e) {
            }
            if (declaredField == null) {
                boolean matchesIESRule = PLURAL_IES_PATTERN.matcher(classname).matches();
                String childNamePlural = matchesIESRule ? classname.substring(0, classname.length() - 1) + "ies" : classname + "s";
                try {
                    declaredField = currentNode.getClass().getDeclaredField(childNamePlural);
                } catch (NoSuchFieldException e) {
                }
                if (declaredField == null) {
                    throw new IllegalStateException("find field by name failed - " + classname);
                }else if (!Collection.class.isAssignableFrom(declaredField.getType())){
                    throw new IllegalStateException("find field by plural name, but field is not a collection  - " + classname);
                }
            }

            Class klass = declaredField.getType();

            if (Collection.class.isAssignableFrom(klass)) {
                Type type = declaredField.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) type;
                    Type[] actualTypeArguments = ptype.getActualTypeArguments();
                    if (actualTypeArguments.length != 1) {
                        throw new RuntimeException("can't determine class name for collection field " + classname + " with multiple generics");
                    }

                    Type typeArgument = actualTypeArguments[0];
                    if (typeArgument instanceof Class) {
                        klass = (Class) actualTypeArguments[0];
                    } else if (typeArgument instanceof ParameterizedType){
                        klass  =((Class)((ParameterizedType)actualTypeArguments[0]).getRawType());
                    }else{
                        throw new RuntimeException("can't instantiate collection field " + classname + " elements as they aren't a class");
                    }
                } else {
                    throw new RuntimeException("collection field " + classname + " must be genericised");
                }
            }

            if(klass.isInterface() || Modifier.isAbstract(klass.getModifiers()))
                throw new IllegalStateException("class is interface of abstract - "+classname);
            logger.debug("resolved className '"+classname+"' success by reflection resolver.");
            return klass.getName();

        }
    }
}
