package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ReflectionUtil {

    public static boolean ignoreField(final Field field) {
        //Ignore private fields.
        if (!Modifier.isPublic(field.getModifiers())) {
            return true;
        }
        //Inner class with reference to outer class.
        if (field.isSynthetic()) {
            return true;
        }
        //Ignore fields with @JsonIgnore annotation
        final JsonIgnore ignore = (JsonIgnore) field.getAnnotation(JsonIgnore.class);
        return ignore != null && ignore.value();
    }

    public static String getPropertyName(final Field field) {
        final JsonProperty property = (JsonProperty) field.getAnnotation(JsonProperty.class);
        return property != null && !property.value().isEmpty() ? property.value() : field.getName();
    }

    public static Object getFieldValue(final Field field, final Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (final IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Method getFirstMethod(final Class type, final Class<? extends Annotation> annotation) {
        for (final Method method : type.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method;
            }
        }
        return null;
    }

    public static Constructor getFirstConstructor(final Class type, final Class<? extends Annotation> annotation) {
        for (final Constructor constructor : type.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(annotation)) {
                return constructor;
            }
        }
        return null;
    }

    public static boolean hasMethod(final Class type, final Class<? extends Annotation> annotation) {
        return getFirstMethod(type, annotation) != null;
    }

    public static boolean hasAnyGetterAndAnySetter(final Class type) {
        return hasMethod(type, JsonAnyGetter.class)
                && hasMethod(type, JsonAnySetter.class);
    }

    public static Class getGenericValueType(final Field field) {
        final Type genericType = field.getGenericType();
        //Generic type
        if (genericType instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType) genericType;
            final Type[] paramTypes = paramType.getActualTypeArguments();
            //Use last index instead of [0] for map where value is at [1]
            return (Class) paramTypes[paramTypes.length - 1];
        }
        //No generic type. Defaults to Object.
        return Object.class;
    }

    public static <T extends Annotation> T getFirstAnotation(Class classType, Class<T> anotType) {
        while (classType != null) {
            final T anot = (T) classType.getAnnotation(anotType);
            if (anot != null) {
                return anot;
            }
            classType = classType.getSuperclass();
        }
        return null;
    }

    public static List<Field> getFieldsInOrder(final Class type) {
        final List<Field> fields = new ArrayList();
        getFields(fields, type);
        final JsonPropertyOrder order = ReflectionUtil.getFirstAnotation(type, JsonPropertyOrder.class);
        if (order != null) {
            sortFields(fields, order);
        }
        return fields;
    }

    private static void getFields(final List<Field> result, final Class type) {
        //Parse super classes first.
        if (type.getSuperclass() != null) {
            getFields(result, type.getSuperclass());
        }
        for (final Field field : type.getDeclaredFields()) {
            if (!ReflectionUtil.ignoreField(field)) {
                result.add(field);
            }
        }
    }

    private static void sortFields(final List<Field> fields, final JsonPropertyOrder order) {
        final Map<String, Integer> orderMap = new HashMap();
        for (final String propertyName : order.value()) {
            orderMap.put(propertyName, orderMap.size());
        }
        fields.sort((final Field o1, final Field o2) -> {
            final String n1 = ReflectionUtil.getPropertyName(o1);
            final String n2 = ReflectionUtil.getPropertyName(o2);
            final Integer i1 = orderMap.get(n1);
            final Integer i2 = orderMap.get(n2);
            if (i1 != null && i2 != null) {
                return i1 - i2;
            }
            if (i1 != null) {
                return -1;
            }
            if (i2 != null) {
                return 1;
            }
            if (order.alphabetic()) {
                return n1.compareTo(n2);
            }
            return 0;
        });
    }

}
