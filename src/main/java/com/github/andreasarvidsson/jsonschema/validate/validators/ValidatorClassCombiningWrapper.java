package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorClassCombiningWrapper {

    public final String path, fieldName;
    public final Object instance;
    public final JsonSchema jsonSchema;

    public ValidatorClassCombiningWrapper(
            final String path,
            final String fieldName,
            final Object instance, final JsonSchema constraintType) {
        this.path = path;
        this.fieldName = fieldName;
        this.instance = instance;
        this.jsonSchema = constraintType;
    }

}
