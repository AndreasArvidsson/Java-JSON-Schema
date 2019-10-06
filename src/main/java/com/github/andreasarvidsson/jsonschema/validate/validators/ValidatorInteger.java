package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorInteger implements Validator {

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        final long value = ((Number) instance).longValue();
        validateMinimum(errors, path, instance, jsonSchema, value);
        validateMaximum(errors, path, instance, jsonSchema, value);
        validateExclusiveMinimum(errors, path, instance, jsonSchema, value);
        validateExclusiveMaximum(errors, path, instance, jsonSchema, value);
        //    long multipleOf() default 0;
    }

    private void validateMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final long value) {
        if (!Objects.equals(jsonSchema.minimum(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.MINIMUM))) {
            if (value < jsonSchema.minimum()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MINIMUM.toString(),
                        jsonSchema.minimum(),
                        String.format("Must have a minimum value of %d", jsonSchema.minimum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final long value) {
        if (!Objects.equals(jsonSchema.maximum(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.MAXIMUM))) {
            if (value > jsonSchema.maximum()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MAXIMUM.toString(),
                        jsonSchema.maximum(),
                        String.format("Must have a maximum value of %d", jsonSchema.maximum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateExclusiveMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final long value) {
        if (!Objects.equals(jsonSchema.exclusiveMinimum(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.EXCLUSIVE_MINIMUM))) {
            if (value <= jsonSchema.exclusiveMinimum()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                        jsonSchema.exclusiveMinimum(),
                        String.format("Must have an exclusive minimum value of %d", jsonSchema.exclusiveMinimum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateExclusiveMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final long value) {
        if (!Objects.equals(jsonSchema.exclusiveMaximum(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.EXCLUSIVE_MAXIMUM))) {
            if (value >= jsonSchema.exclusiveMaximum()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                        jsonSchema.exclusiveMaximum(),
                        String.format("Must have an exclusive maximum value of %d", jsonSchema.exclusiveMaximum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

}
