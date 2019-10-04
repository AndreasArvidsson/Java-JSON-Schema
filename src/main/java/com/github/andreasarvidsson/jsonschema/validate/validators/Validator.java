package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Validator {

    public void validate(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema);

    public void validate(final List<Error> errors, final String path, final Object instance);

}
