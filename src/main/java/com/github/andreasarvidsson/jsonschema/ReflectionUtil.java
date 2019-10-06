package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
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

    public static String getFieldName(final Field field) {
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

}
