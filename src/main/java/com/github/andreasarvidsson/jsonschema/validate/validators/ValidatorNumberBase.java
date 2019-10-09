package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ValidatorNumberBase implements Validator {

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        validateMinimum(errors, path, instance, jsonSchema);
        validateMaximum(errors, path, instance, jsonSchema);
        validateExclusiveMinimum(errors, path, instance, jsonSchema);
        validateExclusiveMaximum(errors, path, instance, jsonSchema);
        validateMultipleOf(errors, path, instance, jsonSchema);
        validateConst(errors, path, instance, jsonSchema);
    }

    protected abstract int compareTo(final Number instance, final String value);

    protected abstract boolean multipleOf(final Number instance, final String value);

    protected abstract Number getArgument(final String value);

    private void validateMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.minimum().isEmpty()) {
            if (compareTo((Number) instance, jsonSchema.minimum()) == -1) {
                errors.add(Error.minimum(path, jsonSchema, getArgument(jsonSchema.minimum()), instance));
            }
        }
    }

    private void validateMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.maximum().isEmpty()) {
            if (compareTo((Number) instance, jsonSchema.maximum()) == 1) {
                errors.add(Error.maximum(path, jsonSchema, getArgument(jsonSchema.maximum()), instance));
            }
        }
    }

    private void validateExclusiveMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.exclusiveMinimum().isEmpty()) {
            if (compareTo((Number) instance, jsonSchema.exclusiveMinimum()) != 1) {
                errors.add(Error.exclusiveMinimum(path, jsonSchema, getArgument(jsonSchema.exclusiveMinimum()), instance));
            }
        }
    }

    private void validateExclusiveMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.exclusiveMaximum().isEmpty()) {
            if (compareTo((Number) instance, jsonSchema.exclusiveMaximum()) != -1) {
                errors.add(Error.exclusiveMaximum(path, jsonSchema, getArgument(jsonSchema.exclusiveMaximum()), instance));
            }
        }
    }

    private void validateMultipleOf(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.multipleOf().isEmpty()) {
            if (!multipleOf((Number) instance, jsonSchema.multipleOf())) {
                errors.add(Error.multipleOf(path, jsonSchema, getArgument(jsonSchema.multipleOf()), instance));
            }
        }
    }

    private void validateConst(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.constant().isEmpty()) {
            if (compareTo((Number) instance, jsonSchema.constant()) != 0) {
                errors.add(Error.constant(path, jsonSchema, getArgument(jsonSchema.constant()), instance));
            }
        }
    }

}
