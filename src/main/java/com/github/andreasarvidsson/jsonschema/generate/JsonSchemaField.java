package com.github.andreasarvidsson.jsonschema.generate;

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
//  REQUIRED - Special case not handled by parsers
//  DEPENDENCIES  - Special case not handled by parsers
//  PATTERN_PROPERTIES - Use type of Map. Dont enable loose typing validation.
//  propertyNames / pattern: String - Not implemented yet

    //Array
    MIN_ITEMS,
    MAX_ITEMS,
//  UNIQUE_ITEMS - Added automatically for sets    
    
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
