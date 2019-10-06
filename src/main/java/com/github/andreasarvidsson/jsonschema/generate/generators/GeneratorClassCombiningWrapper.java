package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorClassCombiningWrapper {

    public final String fieldName;
    public final ObjectNode node;
    public final JsonSchema jsonSchema;
    public final boolean ownProperty;

    public GeneratorClassCombiningWrapper(
            final String fieldName, final ObjectNode constraintNode,
            final JsonSchema constraintType, final boolean ownProperty) {
        this.fieldName = fieldName;
        this.node = constraintNode;
        this.jsonSchema = constraintType;
        this.ownProperty = ownProperty;
    }

}
