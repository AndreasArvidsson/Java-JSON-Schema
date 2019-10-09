package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorString implements Validator {

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        validateMinLength(errors, path, instance, jsonSchema);
        validateMaxLength(errors, path, instance, jsonSchema);
        validatePattern(errors, path, instance, jsonSchema);
    }

    private void validateMinLength(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (jsonSchema.minLength() != Long.MIN_VALUE) {
            if (instance.toString().length() < jsonSchema.minLength()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MIN_LENGTH.toString(),
                        jsonSchema.minLength(),
                        String.format("Does not meet minimum length of %d", jsonSchema.minLength()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateMaxLength(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (jsonSchema.maxLength() != Long.MIN_VALUE) {
            if (instance.toString().length() > jsonSchema.maxLength()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MAX_LENGTH.toString(),
                        jsonSchema.maxLength(),
                        String.format("Does not meet maximum length of %d", jsonSchema.maxLength()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validatePattern(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.pattern().isEmpty()) {
            if (!Pattern.compile(jsonSchema.pattern()).matcher(instance.toString()).matches()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.PATTERN.toString(),
                        jsonSchema.pattern(),
                        String.format("Does not match pattern '%s'", jsonSchema.pattern()),
                        jsonSchema,
                        instance.toString()
                ));
            }
        }
    }

}
