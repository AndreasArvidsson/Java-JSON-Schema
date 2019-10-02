package com.github.andreasarvidsson.jsonschemaform;

/**
 *
 * @author Andreas Arvidsson
 */
public enum JsonSchemaField {

    //General
    TITLE,
    DESCRIPTION,
    
    //Object
    MIN_PROPERTIES,
    MAX_PROPERTIES,
    //propertyNames / pattern: String
    //dependencies: Map<String, String[]>
    
    //Array
    MIN_ITEMS,
    MAX_ITEMS,
    
    //String
    MIN_LENGTH,
    MAX_LENGTH,
    PATTERN,
    FORMAT,
    
    //Number / integer
    MINIMUM,
    MAXIMUM,
    EXCLUSIVE_MINIMUM,
    EXCLUSIVE_MAXIMUM,
    MULTIPLE_OF;

    @Override
    public String toString() {
        final String[] parts = super.toString().toLowerCase().split("_");
        for (int i = 1; i < parts.length; ++i) {
            parts[i] = capitalize(parts[i]);
        }
        return String.join("", parts);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
