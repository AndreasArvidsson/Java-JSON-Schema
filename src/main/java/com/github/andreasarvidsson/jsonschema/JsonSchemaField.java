package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author Andreas Arvidsson
 */
public enum JsonSchemaField {
    // General
    TITLE,
    DESCRIPTION,
    DEPRECATED,
    CONST("constant"),

    // Object
    MIN_PROPERTIES,
    MAX_PROPERTIES,
    // propertyNames / pattern: String - Not implemented yet

    // Array
    MIN_ITEMS,
    MAX_ITEMS,

    // String
    MIN_LENGTH,
    MAX_LENGTH,
    PATTERN,
    FORMAT,

    // Number / integer
    MINIMUM,
    MAXIMUM,
    EXCLUSIVE_MINIMUM,
    EXCLUSIVE_MAXIMUM,
    MULTIPLE_OF;

    private final String value, fieldName;

    private JsonSchemaField() {
        this.value = JsonSchemaField.toString(super.toString());
        this.fieldName = this.value;
    }

    private JsonSchemaField(final String fieldName) {
        this.value = JsonSchemaField.toString(super.toString());
        this.fieldName = fieldName;
    }

    @Override
    @JsonValue()
    public String toString() {
        return value;
    }

    public String getFieldName() {
        return fieldName;
    }

    private static String toString(final String str) {
        final String[] parts = str.toLowerCase().split("_");
        for (int i = 1; i < parts.length; ++i) {
            parts[i] = capitalize(parts[i]);
        }
        return String.join("", parts);
    }

    private static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    // Following are field that are part of the json schema standard bu not
    // applicable as generator 'schemaFields'.
    public static enum Disabled {
        REQUIRED, // Not used by geneators directly. Only class generator.
        DEPENDENCIES, // Se above.
        PATTERN_PROPERTIES, // Not used by geneators directly. Only map generator.
        UNIQUE_ITEMS, // Not used by geneators directly. Only set generator.
        ADDITIONAL_PROPERTIES, // Not used by geneators directly. Only class generator.
        // Below is not validation fields
        PROPERTIES,
        ITEMS,
        ENUM,
        CONST,
        TYPE,
        DEFINITIONS,
        REF("$ref"),
        SCHEMA("$schema"),
        ANY_MATCH("^.*$");

        private final String value;

        private Disabled() {
            this.value = JsonSchemaField.toString(super.toString());
        }

        private Disabled(final String value) {
            this.value = value;
        }

        @Override
        @JsonValue()
        public String toString() {
            return value;
        }

    }

}