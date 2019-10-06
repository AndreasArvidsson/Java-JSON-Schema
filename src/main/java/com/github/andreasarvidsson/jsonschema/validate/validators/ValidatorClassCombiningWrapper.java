package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorClassCombiningWrapper {

    public final String fieldName;
    public final JsonSchema jsonSchema;
    public final boolean ownProperty;

    public ValidatorClassCombiningWrapper(
            final String fieldName,
            final JsonSchema constraintType, final boolean ownProperty) {
        this.fieldName = fieldName;
        this.jsonSchema = constraintType;
        this.ownProperty = ownProperty;
    }

}
