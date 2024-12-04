package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorChar implements Validator {

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance,
            final JsonSchema jsonSchema) {
        validateConst(errors, path, instance, jsonSchema);
    }

    private void validateConst(final List<Error> errors, final String path, final Object instance,
            final JsonSchema jsonSchema) {
        if (!jsonSchema.constant().isEmpty()) {
            if (jsonSchema.constant().charAt(0) != (char) instance) {
                errors.add(Error.constant(path, jsonSchema, jsonSchema.constant().charAt(0), instance));
            }
        }
    }

}
