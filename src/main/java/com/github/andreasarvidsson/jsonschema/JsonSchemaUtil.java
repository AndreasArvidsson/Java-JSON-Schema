package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class JsonSchemaUtil {

    private static final Map<JsonSchemaField, Object> DEFAULT_VALUES = new HashMap<>();
    private static final Set<String> VALIDATE_FIELDS = new HashSet<>();

    static {
        for (final JsonSchemaField field : JsonSchemaField.values()) {
            try {
                DEFAULT_VALUES.put(field, JsonSchema.class.getMethod(field.getFieldName()).getDefaultValue());
                VALIDATE_FIELDS.add(field.toString());
            } catch (final NoSuchMethodException | SecurityException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void validateAllowedFields(final Class type, final Set<String> allowed, final JsonSchema jsonSchema) {
        for (final Method method : JsonSchema.class.getDeclaredMethods()) {
            try {
                if (!VALIDATE_FIELDS.contains(method.getName())) {
                    continue;
                }
                // Not defualt vlaue and not allowed
                if (!Objects.equals(method.getDefaultValue(), method.invoke(jsonSchema))
                        && !allowed.contains(method.getName())) {
                    throw new RuntimeException(
                            String.format("Json schema field '%s' is not applicable for type '%s'",
                                    method.getName(), type.getTypeName()));
                }
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static Map<String, Object> toMap(final JsonSchema jsonSchema) {
        final Map<String, Object> res = new LinkedHashMap<>();
        for (final Method method : JsonSchema.class.getDeclaredMethods()) {
            final Object value = getValue(method, jsonSchema);
            if (value.getClass().isArray()) {
                if (((Object[]) value).length > 0) {
                    res.put(method.getName(), value);
                }
            } else {
                if (!Objects.equals(value, method.getDefaultValue())) {
                    res.put(method.getName(), value);
                }
            }
        }
        return res;
    }

    public static void setString(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final String value) {
        if (!value.isEmpty()) {
            target.put(field.toString(), value);
        }
    }

    public static void setBool(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final String value) {
        if (!value.isEmpty()) {
            target.put(field.toString(), Boolean.parseBoolean(value));
        }
    }

    public static void setNumbers(
            final Class type, final ObjectNode target,
            final JsonSchemaField field1, final String value1,
            final JsonSchemaField field2, final String value2) {
        final boolean b1 = setNumberIfNotDefault(target, field1, value1);
        final boolean b2 = setNumberIfNotDefault(target, field2, value2);
        if (b1 && b2) {
            validateMaxLargerThanMin(type, false,
                    field1, Double.parseDouble(value1),
                    field2, Double.parseDouble(value2));
        }
    }

    public static void setNumber(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final String value) {
        setNumberIfNotDefault(target, field, value);
    }

    public static void setIntegers(
            final Class type, final ObjectNode target,
            final JsonSchemaField field1, final String value1, final long min1, final long max1,
            final JsonSchemaField field2, final String value2, final long min2, final long max2) {
        final boolean b1 = setIntegerIfNotDefault(type, target, field1, value1, min1, max1);
        final boolean b2 = setIntegerIfNotDefault(type, target, field2, value2, min2, max2);
        if (b1 && b2) {
            validateMaxLargerThanMin(type, true,
                    field1, Long.parseLong(value1),
                    field2, Long.parseLong(value2));
        }
    }

    public static void setIntegers(
            final Class type, final ObjectNode target,
            final JsonSchemaField field1, final long value1, final long min1, final long max1,
            final JsonSchemaField field2, final long value2, final long min2, final long max2) {
        final boolean b1 = setIntegerIfNotDefault(type, target, field1, value1, min1, max1);
        final boolean b2 = setIntegerIfNotDefault(type, target, field2, value2, min2, max2);
        if (b1 && b2) {
            validateMaxLargerThanMin(type, true, field1, value1, field2, value2);
        }
    }

    public static void setInteger(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final String value, final long min, final long max) {
        setIntegerIfNotDefault(type, target, field, value, min, max);
    }

    private static boolean setNumberIfNotDefault(final ObjectNode target, final JsonSchemaField field,
            final String value) {
        // If still default value. Just stop/return.
        if (value.isEmpty()) {
            return false;
        }
        target.put(field.toString(), Double.parseDouble(value));
        return true;
    }

    private static boolean setIntegerIfNotDefault(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final String value, final long min, final long max) {
        // If still default value. Just stop/return.
        if (value.isEmpty()) {
            return false;
        }
        return setIntegerIfNotDefault(type, target, field, Long.parseLong(value), min, max);
    }

    private static boolean setIntegerIfNotDefault(
            final Class type, final ObjectNode target,
            final JsonSchemaField field, final long value, final long min, final long max) {
        // If still default value. Just stop/return.
        if (value == Long.MIN_VALUE) {
            return false;
        }
        validateRange(type, field, value, min, max);
        target.put(field.toString(), value);
        return true;
    }

    private static void validateRange(final Class type, final JsonSchemaField field, final long value, final long min,
            final long max) {
        if (value < min || value > max) {
            throw new RuntimeException(String.format(
                    "Json schema field %s value %d is out of range [%d, %d] for type '%s'",
                    field.toString(), value, min, max, type.getTypeName()));
        }
    }

    private static void validateMaxLargerThanMin(
            final Class type, final boolean isInteger,
            final JsonSchemaField fieldMin, final Number valueMin,
            final JsonSchemaField fieldMax, final Number valueMax) {
        boolean isInvalid;
        if (isInteger) {
            isInvalid = valueMin.longValue() >= valueMax.longValue();
        } else {
            isInvalid = valueMin.doubleValue() >= valueMax.doubleValue();
        }
        if (isInvalid) {
            throw new RuntimeException(String.format(
                    "Json schema field %s=%d is larger or equals to %s=%d for type '%s'",
                    fieldMin.toString(), valueMin, fieldMax.toString(), valueMax, type.getTypeName()));
        }
    }

    private static Number toNumber(final String value, final boolean isInteger) {
        return isInteger ? Long.parseLong(value) : Double.parseDouble(value);
    }

    private static Object getValue(final Method method, final JsonSchema jsonSchema) {
        try {
            return method.invoke(jsonSchema);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

}
