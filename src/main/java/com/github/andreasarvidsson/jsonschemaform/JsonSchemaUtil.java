package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class JsonSchemaUtil {

    public static void addFields(final Class type, final ObjectNode res, final AnnotatedElement elem, final Set<JsonSchemaField> schemaFields) {
        final JsonSchema[] anotations = elem.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE) {
                //General
                set(type, schemaFields, res, JsonSchemaField.TITLE, anot.title());
                set(type, schemaFields, res, JsonSchemaField.DESCRIPTION, anot.description());

                //Object
                set(type, schemaFields, res, JsonSchemaField.MIN_PROPERTIES, anot.minProperties());
                set(type, schemaFields, res, JsonSchemaField.MAX_PROPERTIES, anot.maxProperties());

                //Array
                set(type, schemaFields, res, JsonSchemaField.MIN_ITEMS, anot.minItems());
                set(type, schemaFields, res, JsonSchemaField.MAX_ITEMS, anot.maxItems());

                //String
                set(type, schemaFields, res, JsonSchemaField.MIN_LENGTH, anot.minLength());
                set(type, schemaFields, res, JsonSchemaField.MAX_LENGTH, anot.maxLength());
                set(type, schemaFields, res, JsonSchemaField.PATTERN, anot.pattern());
//                set(schemaFields, res, JsonSchemaField.FORMAT, anot.format());

                //Number / integer
                set(type, schemaFields, res, JsonSchemaField.MINIMUM, anot.minimum());
                set(type, schemaFields, res, JsonSchemaField.MAXIMUM, anot.maximum());
                set(type, schemaFields, res, JsonSchemaField.EXCLUSIVE_MINIMUM, anot.exclusiveMinimum());
                set(type, schemaFields, res, JsonSchemaField.EXCLUSIVE_MAXIMUM, anot.exclusiveMaximum());
                set(type, schemaFields, res, JsonSchemaField.MULTIPLE_OF, anot.multipleOf());
            }
        }
    }

    public static boolean isRequired(final Field field) {
        return field.getType().isPrimitive() || isAnotRequired(field);
    }

    public static boolean isRequired(final Class type) {
        return type.isPrimitive();
    }

    private static boolean isAnotRequired(final Field field) {
        final JsonSchema[] anotations = field.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE && anot.required()) {
                return true;
            }
        }
        return false;
    }

    private static void set(
            final Class type, final Collection<JsonSchemaField> schemaFields,
            final ObjectNode result, final JsonSchemaField field, final Object value) {
        //If still default value. Just stop/return.
        if (Objects.equals(getDefaultValue(field), value)) {
            return;
        }
        //New value. Check if valid for this node.
        if (!schemaFields.contains(field)) {
            throw new RuntimeException(String.format("Json schema field '%s' is not applicable for '%s'", field.toString(), type.getTypeName()));
        }
        result.putPOJO(field.toString(), value);
    }

    private static Object getDefaultValue(final JsonSchemaField field) {
        try {
            return JsonSchema.class
                    .getDeclaredMethod(field.toString())
                    .getDefaultValue();
        }
        catch (final NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

}
