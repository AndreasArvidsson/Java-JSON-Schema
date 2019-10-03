package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class JsonSchemaUtil {

    private static final Map<JsonSchemaField, Object> DEFAULT_VALUES = new HashMap();

    static {
        try {
            for (final JsonSchemaField field : JsonSchemaField.values()) {
                final Object defaultValue = JsonSchema.class
                        .getDeclaredMethod(field.toString())
                        .getDefaultValue();
                DEFAULT_VALUES.put(field, defaultValue);
            }
        }
        catch (final NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void addFields(
            final Class type, final ObjectNode target, final AnnotatedElement elem,
            final Set<JsonSchemaField> allowed) {
        final JsonSchema[] anotations = elem.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE) {
                //General
                set(type, allowed, target, JsonSchemaField.TITLE, anot.title());
                set(type, allowed, target, JsonSchemaField.DESCRIPTION, anot.description());

                //Object
                set(type, allowed, target, JsonSchemaField.MIN_PROPERTIES, anot.minProperties());
                set(type, allowed, target, JsonSchemaField.MAX_PROPERTIES, anot.maxProperties());
                //required is a special case used by the class parser and not by each type parser.
                //dependencies: Se above

                //Array
                set(type, allowed, target, JsonSchemaField.MIN_ITEMS, anot.minItems());
                set(type, allowed, target, JsonSchemaField.MAX_ITEMS, anot.maxItems());

                //String
                set(type, allowed, target, JsonSchemaField.MIN_LENGTH, anot.minLength());
                set(type, allowed, target, JsonSchemaField.MAX_LENGTH, anot.maxLength());
                set(type, allowed, target, JsonSchemaField.PATTERN, anot.pattern());
                set(type, allowed, target, JsonSchemaField.FORMAT, anot.format());

                //Number / integer
                set(type, allowed, target, JsonSchemaField.MINIMUM, anot.minimum());
                set(type, allowed, target, JsonSchemaField.MAXIMUM, anot.maximum());
                set(type, allowed, target, JsonSchemaField.EXCLUSIVE_MINIMUM, anot.exclusiveMinimum());
                set(type, allowed, target, JsonSchemaField.EXCLUSIVE_MAXIMUM, anot.exclusiveMaximum());
                set(type, allowed, target, JsonSchemaField.MULTIPLE_OF, anot.multipleOf());
            }
        }
    }

    public static boolean isRequired(final Field field) {
        if (field.getType().isPrimitive()) {
            return true;
        }
        final JsonSchema[] anotations = field.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE && anot.required()) {
                return true;
            }
        }
        return false;
    }

    public static String[] getDependencies(final Field field) {
        final JsonSchema[] anotations = field.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE && anot.dependencies().length > 0) {
                return anot.dependencies();
            }
        }
        return new String[]{};
    }

    private static void set(
            final Class type, final Collection<JsonSchemaField> allowed,
            final ObjectNode target, final JsonSchemaField field, final Object value) {
        //If still default value. Just stop/return.
        if (Objects.equals(DEFAULT_VALUES.get(field), value)) {
            return;
        }
        //New value. Check if valid for this node.
        if (!allowed.contains(field)) {
            throw new RuntimeException(String.format("Json schema field '%s' is not applicable for '%s'", field.toString(), type.getTypeName()));
        }
        target.putPOJO(field.toString(), value);
    }

}
