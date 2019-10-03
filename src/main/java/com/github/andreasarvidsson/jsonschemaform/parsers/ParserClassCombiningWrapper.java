package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserClassCombiningWrapper {

    public final String fieldName;
    public final ObjectNode node;
    public final JsonSchema jsonSchema;
    public final boolean ownProperty;

    public ParserClassCombiningWrapper(
            final String fieldName, final ObjectNode constraintNode,
            final JsonSchema constraintType, final boolean ownProperty) {
        this.fieldName = fieldName;
        this.node = constraintNode;
        this.jsonSchema = constraintType;
        this.ownProperty = ownProperty;
    }

}
