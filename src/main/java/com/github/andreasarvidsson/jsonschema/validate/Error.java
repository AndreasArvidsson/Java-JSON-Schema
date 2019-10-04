package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import java.util.LinkedHashMap;
import java.util.Map;

public class Error {

    public final String property;
    public final Name name;
    public final String argument;
    public final String message;
    private final JsonSchema schema;
    public final Object instance;
//    public final Map<String, String> instance;
//    public final Map<String, String> schema;
//    public final Map<String, String> instance;

    public Error(
            final String property, final Name name, final String argument,
            final String message, final JsonSchema schema, final Object instance) {
        this.property = property;
        this.name = name;
        this.argument = argument;
        this.message = message;
        this.schema = schema;
        this.instance = instance;
//        this.schema = new LinkedHashMap();
//        this.instance = new HashMap();
//        this.instance.put("pointer", path);
//        this.schema.put("loadingURI", "#");
//        this.schema.put("pointer", schemaPath);
    }

    public Map<String, Object> getSchema() {
        final Map<String, Object> res = new LinkedHashMap();
        if (schema.required()) {
            res.put("required", schema.required());
        }
        return res;
    }

    public static enum Name {
        REQUIRED,
        MAXIMUM,
        MINIMUM,
        PATTERN,
        ONEOF,
        ANYOF,
        ALLOFF;

        @Override
        @JsonValue
        public String toString() {
            return super.toString().toLowerCase();
        }

        public boolean equals(String str) {
            return toString().equals(str);
        }

    }

}
