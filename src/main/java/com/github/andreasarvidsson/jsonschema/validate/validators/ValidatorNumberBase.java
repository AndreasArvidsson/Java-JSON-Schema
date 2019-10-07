package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
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
    }

    protected abstract int compareTo(final Object instance, final String value);

    protected abstract boolean multipleOf(final Object instance, final String value);

    protected abstract Object getArgument(final String value);

    private void validateMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.minimum().isEmpty()) {
            if (compareTo(instance, jsonSchema.minimum()) == -1) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MINIMUM.toString(),
                        getArgument(jsonSchema.minimum()),
                        String.format("Must have a minimum value of %s", jsonSchema.minimum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.maximum().isEmpty()) {
            if (compareTo(instance, jsonSchema.maximum()) == 1) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MAXIMUM.toString(),
                        getArgument(jsonSchema.maximum()),
                        String.format("Must have a maximum value of %s", jsonSchema.maximum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateExclusiveMinimum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.exclusiveMinimum().isEmpty()) {
            if (compareTo(instance, jsonSchema.exclusiveMinimum()) != 1) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                        getArgument(jsonSchema.exclusiveMinimum()),
                        String.format("Must have an exclusive minimum value of %s", jsonSchema.exclusiveMinimum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateExclusiveMaximum(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.exclusiveMaximum().isEmpty()) {
            if (compareTo(instance, jsonSchema.exclusiveMaximum()) != -1) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                        getArgument(jsonSchema.exclusiveMaximum()),
                        String.format("Must have an exclusive maximum value of %s", jsonSchema.exclusiveMaximum()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateMultipleOf(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.multipleOf().isEmpty()) {
            if (!multipleOf(instance, jsonSchema.multipleOf())) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MULTIPLE_OF.toString(),
                        getArgument(jsonSchema.multipleOf()),
                        String.format("Is not a multiple of (divisible by) %s", jsonSchema.multipleOf()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

}
