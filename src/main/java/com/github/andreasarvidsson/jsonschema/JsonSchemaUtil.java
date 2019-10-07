package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    public static Object getDefaultValue(final JsonSchemaField field) {
        return DEFAULT_VALUES.get(field);
    }

    public static void addFields(
            final Class type, final ObjectNode target,
            final Set<JsonSchemaField> allowed) {
        final JsonSchema[] anotations = (JsonSchema[]) type.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema jsonSchema : anotations) {
            if (jsonSchema.combining() != JsonSchema.Combining.NONE) {
                throw new RuntimeException(String.format("Schema combinings(anyOf, oneOf, allOf) is not allowed on class level. '%s'", type.getTypeName()));
            }
            addFields(type, target, allowed, jsonSchema);
        }
    }

    public static void addFields(
            final Class type, final ObjectNode target,
            final Set<JsonSchemaField> allowed, final JsonSchema jsonSchema) {
        //General
        set(type, allowed, target, JsonSchemaField.TITLE, jsonSchema.title());
        set(type, allowed, target, JsonSchemaField.DESCRIPTION, jsonSchema.description());

        //Object
        set(target, type, allowed,
                JsonSchemaField.MIN_PROPERTIES, jsonSchema.minProperties(), 0, Integer.MAX_VALUE,
                JsonSchemaField.MAX_PROPERTIES, jsonSchema.maxProperties(), 1, Integer.MAX_VALUE
        );
        //required is a special case used by the class parser and not by each type parser.
        //dependencies: Se above

        //Array
        set(target, type, allowed,
                JsonSchemaField.MIN_ITEMS, jsonSchema.minItems(), 0, Integer.MAX_VALUE,
                JsonSchemaField.MAX_ITEMS, jsonSchema.maxItems(), 1, Integer.MAX_VALUE
        );

        //String
        set(target, type, allowed,
                JsonSchemaField.MIN_LENGTH, jsonSchema.minLength(), 0, Integer.MAX_VALUE,
                JsonSchemaField.MAX_LENGTH, jsonSchema.maxLength(), 1, Integer.MAX_VALUE
        );
        set(type, allowed, target, JsonSchemaField.PATTERN, jsonSchema.pattern());
        set(type, allowed, target, JsonSchemaField.FORMAT, jsonSchema.format());

        //Number / integer
        set(type, allowed, target, JsonSchemaField.MINIMUM, jsonSchema.minimum());
        set(type, allowed, target, JsonSchemaField.MAXIMUM, jsonSchema.maximum());
        set(type, allowed, target, JsonSchemaField.EXCLUSIVE_MINIMUM, jsonSchema.exclusiveMinimum());
        set(type, allowed, target, JsonSchemaField.EXCLUSIVE_MAXIMUM, jsonSchema.exclusiveMaximum());
        set(type, allowed, target, JsonSchemaField.MULTIPLE_OF, jsonSchema.multipleOf());
    }

    public static Map<String, Object> toMap(final JsonSchema jsonSchema) {
        final Map<String, Object> res = new LinkedHashMap();
        for (final Method method : JsonSchema.class.getDeclaredMethods()) {
            final Object value = getValue(method, jsonSchema);
            if (value.getClass().isArray()) {
                if (((Object[]) value).length > 0) {
                    res.put(method.getName(), value);
                }
            }
            else {
                if (!Objects.equals(value, method.getDefaultValue())) {
                    res.put(method.getName(), value);
                }
            }
        }
        return res;
    }

    private static Object getValue(final Method method, final JsonSchema jsonSchema) {
        try {
            return method.invoke(jsonSchema);
        }
        catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void set(
            final ObjectNode target, final Class type, final Collection<JsonSchemaField> allowed,
            final JsonSchemaField field1, final long value1, final long min1, final long max1,
            final JsonSchemaField field2, final long value2, final long min2, final long max2) {
        final boolean b1 = set(target, type, allowed, field1, value1, min1, max1);
        final boolean b2 = set(target, type, allowed, field2, value2, min2, max2);
        if (b1 && b2) {
            validateMaxLargerThanMin(type, field1, field2, value1, value2);
        }
    }

    private static boolean set(
            final ObjectNode target, final Class type, final Collection<JsonSchemaField> allowed,
            final JsonSchemaField field, final long value, final long min, final long max) {
        //If still default value. Just stop/return.
        if (Objects.equals(DEFAULT_VALUES.get(field), value)) {
            return false;
        }
        validateAllowed(type, allowed, field);
        validateRange(type, field, value, min, max);
        target.putPOJO(field.toString(), value);
        return true;
    }

    private static void set(
            final Class type, final Collection<JsonSchemaField> allowed,
            final ObjectNode target, final JsonSchemaField field, final Object value) {
        //If still default value. Just stop/return.
        if (Objects.equals(DEFAULT_VALUES.get(field), value)) {
            return;
        }
        validateAllowed(type, allowed, field);
        target.putPOJO(field.toString(), value);
    }

    private static void validateAllowed(final Class type, final Collection<JsonSchemaField> allowed, final JsonSchemaField field) {
        //New value. Check if valid for this node.
        if (!allowed.contains(field)) {
            throw new RuntimeException(String.format("Json schema field %s is not applicable for type '%s'", field.toString(), type.getTypeName()));
        }
    }

    private static void validateRange(final Class type, final JsonSchemaField field, final long value, final long min, final long max) {
        if (value < min || value > max) {
            throw new RuntimeException(String.format(
                    "Json schema field %s value %d is out of range [%d, %d] for type '%s'",
                    field.toString(), value, min, max, type.getTypeName())
            );
        }
    }

    private static void validateMaxLargerThanMin(
            final Class type, final JsonSchemaField fieldMin, final JsonSchemaField fieldMax,
            final long valueMin, final long valueMax) {
        if (valueMin >= valueMax) {
            throw new RuntimeException(String.format(
                    "Json schema field %s=%d is larger or equals to %s=%d for type '%s'",
                    fieldMin.toString(), valueMin, fieldMax.toString(), valueMax, type.getTypeName())
            );
        }
    }

}
