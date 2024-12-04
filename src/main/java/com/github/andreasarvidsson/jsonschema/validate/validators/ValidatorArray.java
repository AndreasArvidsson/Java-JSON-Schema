package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.lang.reflect.Array;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorArray extends ValidatorArrayBase {

    private final Validators validators;

    public ValidatorArray(final Validators validators) {
        this.validators = validators;
    }

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
        for (int i = 0; i < Array.getLength(instance); ++i) {
            validators.validateClass(errors, PropertyPath.append(path, i), Array.get(instance, i));
        }
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance,
            final JsonSchema jsonSchema) {
        super.validateArray(errors, path, instance, jsonSchema, Array.getLength(instance));
    }

}
