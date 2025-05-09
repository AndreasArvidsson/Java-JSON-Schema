package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorMap implements Validator {

    private final Validators validators;

    public ValidatorMap(final Validators validators) {
        this.validators = validators;
    }

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
        if (instance instanceof Map) {
            for (final Map.Entry<Object, Object> e : ((Map<Object, Object>) instance).entrySet()) {
                validators.validateClass(errors, PropertyPath.append(path, e.getKey().toString()), e.getValue());
            }
        }
    }

    @Override
    public void validateSchema(
            final List<Error> errors,
            final String path,
            final Object instance,
            final JsonSchema jsonSchema) {
        validateMinProperties(errors, path, instance, jsonSchema, ((Map<?, ?>) instance).size());
        validateMaxProperties(errors, path, instance, jsonSchema, ((Map<?, ?>) instance).size());
    }

    private void validateMinProperties(final List<Error> errors, final String path, final Object instance,
            final JsonSchema jsonSchema, final int length) {
        if (jsonSchema.minProperties() != Long.MIN_VALUE && length < jsonSchema.minProperties()) {
            errors.add(Error.minProperties(path, jsonSchema, instance));
        }
    }

    private void validateMaxProperties(final List<Error> errors, final String path, final Object instance,
            final JsonSchema jsonSchema, final int length) {
        if (jsonSchema.maxProperties() != Long.MIN_VALUE && length > jsonSchema.maxProperties()) {
            errors.add(Error.maxProperties(path, jsonSchema, instance));
        }
    }

}
