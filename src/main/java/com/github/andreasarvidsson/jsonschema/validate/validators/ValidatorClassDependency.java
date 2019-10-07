package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorClassDependency {

    public final String path, fieldName;
    public final JsonSchema jsonSchema;

    public ValidatorClassDependency(final String path, final String fieldName, final JsonSchema jsonSchema) {
        this.path = path;
        this.fieldName = fieldName;
        this.jsonSchema = jsonSchema;
    }

}
