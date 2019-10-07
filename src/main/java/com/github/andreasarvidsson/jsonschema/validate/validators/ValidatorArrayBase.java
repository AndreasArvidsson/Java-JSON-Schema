package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ValidatorArrayBase implements Validator {

    protected void validateArray(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final int length) {
        validateMinItems(errors, path, instance, jsonSchema, length);
        validateMaxItems(errors, path, instance, jsonSchema, length);
    }

    private void validateMinItems(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final int length) {
        if (jsonSchema.minItems() != Long.MIN_VALUE && length < jsonSchema.minItems()) {
            errors.add(new Error(
                    path,
                    JsonSchemaField.MIN_ITEMS.toString(),
                    jsonSchema.minItems(),
                    String.format("Does not meet minimum length of %d", jsonSchema.minItems()),
                    jsonSchema,
                    instance
            ));
        }
    }

    private void validateMaxItems(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final int length) {
        if (jsonSchema.maxItems() != Long.MIN_VALUE && length > jsonSchema.maxItems()) {
            errors.add(new Error(
                    path,
                    JsonSchemaField.MAX_ITEMS.toString(),
                    jsonSchema.maxItems(),
                    String.format("Does not meet maximum length of %d", jsonSchema.maxItems()),
                    jsonSchema,
                    instance
            ));
        }
    }

}
