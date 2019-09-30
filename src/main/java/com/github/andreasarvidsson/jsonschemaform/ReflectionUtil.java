package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ReflectionUtil {

    public static boolean ignoreField(final Field field) {
        //Ignore private fields.
        if (Modifier.isPrivate(field.getModifiers())) {
            return true;
        }
        //Inner class with reference to outer class.
        if (field.getName().equals("this$0")) {
            return true;
        }
        //Ignore fields with @JsonIgnore annotation
        final JsonIgnore ignore = (JsonIgnore) field.getAnnotation(JsonIgnore.class);
        return ignore != null && ignore.value();
    }

    public static String getFieldName(final Field field) {
        final JsonProperty property = (JsonProperty) field.getAnnotation(JsonProperty.class);
        return property != null && !property.value().isEmpty() ? property.value() : field.getName();
    }

    public static Method getFirstMethod(final Class type, final Class<? extends Annotation> annotation) {
        for (final Method method : type.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method;
            }
        }
        return null;
    }

    public static boolean hasMethod(final Class type, final Class<? extends Annotation> annotation) {
        return getFirstMethod(type, annotation) != null;
    }

    public static List<Method> getMethods(final Class type, final Class<? extends Annotation> annotation) {
        final List<Method> res = new ArrayList();
        for (final Method method : type.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                res.add(method);
            }
        }
        return res;
    }

    public static boolean hasAnyGetterAndAnySetter(final Class type) {
        return hasMethod(type, JsonAnyGetter.class)
                && hasMethod(type, JsonAnySetter.class);
    }

}
