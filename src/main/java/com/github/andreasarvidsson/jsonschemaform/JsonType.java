package com.github.andreasarvidsson.jsonschemaform;

/**
 *
 * @author Andreas Arvidsson
 */
public enum JsonType {

    ARRAY,
    OBJECT,
    INTEGER,
    NUMBER,
    STRING,
    BOOLEAN,
    NULL;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
