package com.github.andreasarvidsson.jsonschema;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class PropertyPath {

    public static String append(final String path, final String fieldName) {
        return String.format("%s.%s", path, fieldName);
    }

    public static String append(final String path, final int index) {
        return String.format("%s[%d]", path, index);
    }

}
