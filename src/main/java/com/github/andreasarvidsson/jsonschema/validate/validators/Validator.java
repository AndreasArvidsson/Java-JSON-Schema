package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Validator {

    public void validateClass(final List<com.github.andreasarvidsson.jsonschema.validate.Error> errors,
            final String path, final Object instance);

    public void validateSchema(final List<com.github.andreasarvidsson.jsonschema.validate.Error> errors,
            final String path, final Object instance, final JsonSchema jsonSchema);
}
