package com.github.andreasarvidsson.jsonschema.generate;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
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
