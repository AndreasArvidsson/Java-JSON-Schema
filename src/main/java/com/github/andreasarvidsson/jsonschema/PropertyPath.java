package com.github.andreasarvidsson.jsonschema;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class PropertyPath {

    public static String append(final String path, final String propertyName) {
        return String.format("%s.%s", path, propertyName);
    }

    public static String append(final String path, final int propertyIndex) {
        return String.format("%s[%d]", path, propertyIndex);
    }

}
