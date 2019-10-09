package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
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
        validateConst(errors, path, instance, jsonSchema);
    }

    private void validateMinLength(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (jsonSchema.minLength() != Long.MIN_VALUE) {
            if (instance.toString().length() < jsonSchema.minLength()) {
                errors.add(Error.minLength(path, jsonSchema, instance));
            }
        }
    }

    private void validateMaxLength(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (jsonSchema.maxLength() != Long.MIN_VALUE) {
            if (instance.toString().length() > jsonSchema.maxLength()) {
                errors.add(Error.maxLength(path, jsonSchema, instance));
            }
        }
    }

    private void validatePattern(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.pattern().isEmpty()) {
            if (!Pattern.compile(jsonSchema.pattern()).matcher(instance.toString()).matches()) {
                errors.add(Error.pattern(path, jsonSchema, instance));
            }
        }
    }

    private void validateConst(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        if (!jsonSchema.constant().isEmpty()) {
            if (!jsonSchema.constant().equals(instance)) {
                errors.add(Error.constant(path, jsonSchema, jsonSchema.constant(), instance));
            }
        }
    }

}
