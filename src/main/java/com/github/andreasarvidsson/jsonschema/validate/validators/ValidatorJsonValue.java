package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorJsonValue implements Validator {

    private final Method method;
    private final Validator validator;

    public ValidatorJsonValue(final Validator validator, final Method method) {
        this.validator = validator;
        this.method = method;
    }

    @Override
    public void validateClass(final List<Error> errors, final String path, Object instance) {
        for (final JsonSchema jsonSchema : instance.getClass().getAnnotationsByType(JsonSchema.class)) {
            if (jsonSchema.combining() == JsonSchema.Combining.NONE) {
                validateSchema(errors, path, instance, jsonSchema);
            }
        }
        try {
            validator.validateClass(errors, path, method.invoke(instance));
        }
        catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        try {
            validator.validateSchema(errors, path, method.invoke(instance), jsonSchema);
        }
        catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

}
