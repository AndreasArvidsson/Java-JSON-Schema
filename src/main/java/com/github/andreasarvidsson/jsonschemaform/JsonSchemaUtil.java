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

    public static void addFields(final ObjectNode res, final AnnotatedElement elem, final Set<JsonSchemaField> schemaFields) {
        final JsonSchema[] anotations = elem.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema anot : anotations) {
            if (anot.crossFieldConstraint() == CrossFieldConstraint.NONE) {

                //General
                set(schemaFields, res, JsonSchemaField.TITLE, anot.title());
                set(schemaFields, res, JsonSchemaField.DESCRIPTION, anot.description());

                //Object
//            set(res, JsonSchemaField.MIN_PROPERTIES, anot.minProperties());
//            set(res, JsonSchemaField.MAX_PROPERTIES, anot.maxProperties());
                //set(res, JsonSchemaField.REQUIRED, anot.required());
                //Array
                set(schemaFields, res, JsonSchemaField.MIN_ITEMS, anot.minItems());
                set(schemaFields, res, JsonSchemaField.MAX_ITEMS, anot.maxItems());
//                set(schemaFields, res, JsonSchemaField.UNIQUE_ITEMS, anot.uniqueItems());

                //String
                set(schemaFields, res, JsonSchemaField.MIN_LENGTH, anot.minLength());
                set(schemaFields, res, JsonSchemaField.MAX_LENGTH, anot.maxLength());
                set(schemaFields, res, JsonSchemaField.PATTERN, anot.pattern());
//                set(schemaFields, res, JsonSchemaField.FORMAT, anot.format());

                //Number / integer
                set(schemaFields, res, JsonSchemaField.MINIMUM, anot.minimum());
                set(schemaFields, res, JsonSchemaField.MAXIMUM, anot.maximum());
                set(schemaFields, res, JsonSchemaField.EXCLUSIVE_MINIMUM, anot.exclusiveMinimum());
                set(schemaFields, res, JsonSchemaField.EXCLUSIVE_MAXIMUM, anot.exclusiveMaximum());
                set(schemaFields, res, JsonSchemaField.MULTIPLE_OF, anot.multipleOf());
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
            if (anot.crossFieldConstraint() != CrossFieldConstraint.NONE && anot.required()) {
                return true;
            }
        }
        return false;
    }

    private static void set(final Collection<JsonSchemaField> schemaFields, final ObjectNode result, final JsonSchemaField field, final Object value) {
        //If stil default value. Just stop/return.
        if (Objects.equals(getDefaultValue(field), value)) {
            return;
        }
        //New value. Check if valid for this node.
        if (!schemaFields.contains(field)) {
            //throw new RuntimeException(String.format("Field '%s' is not applicable for type '%s'", field.toString(), jsonType.toString()));
            throw new RuntimeException(String.format("Field '%s' is not applicable", field.toString()));
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
