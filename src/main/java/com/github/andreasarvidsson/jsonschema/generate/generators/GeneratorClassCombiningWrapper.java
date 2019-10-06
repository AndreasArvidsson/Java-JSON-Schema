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
            final String fieldName, final ObjectNode node,
            final JsonSchema jsonSchema ,final boolean ownProperty) {
        this.fieldName = fieldName;
        this.node = node;
        this.jsonSchema = jsonSchema;
        this.ownProperty = ownProperty;
    }

}
