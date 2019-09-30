package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class CrossConstraintWrapper {

    public String fieldName;
    public ObjectNode constraintNode;
    JsonSchema constraintType;

    public CrossConstraintWrapper(String fieldName, ObjectNode constraintNode, JsonSchema constraintType) {
        this.fieldName = fieldName;
        this.constraintNode = constraintNode;
        this.constraintType = constraintType;
    }

}
