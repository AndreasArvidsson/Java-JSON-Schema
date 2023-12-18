package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorCollection extends ValidatorArrayBase {

    private final Validators validators;

    public ValidatorCollection(final Validators validators) {
        this.validators = validators;
    }

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
        int i = 0;
        for (final Object o : (Collection) instance) {
            validators.validateClass(errors, PropertyPath.append(path, i++), o);
        }
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        super.validateArray(errors, path, instance, jsonSchema, ((Collection) instance).size());
    }

}
