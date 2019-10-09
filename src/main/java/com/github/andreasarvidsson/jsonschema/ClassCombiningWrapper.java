package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassCombiningWrapper {

    public final String path, propertyName;
    public final JsonSchema jsonSchema;
    public final Object instance;
    public final ObjectNode propertyNode;

    public ClassCombiningWrapper(
            final String path, final String propertyName,
            final JsonSchema constraintType, final Object instance) {
        this.path = path;
        this.propertyName = propertyName;
        this.jsonSchema = constraintType;
        this.instance = instance;
        this.propertyNode = null;
    }

    public ClassCombiningWrapper(
            final String propertyName,
            final JsonSchema constraintType, final ObjectNode propertyNode) {
        this.propertyName = propertyName;
        this.jsonSchema = constraintType;
        this.propertyNode = propertyNode;
        this.path = null;
        this.instance = null;
    }

}
