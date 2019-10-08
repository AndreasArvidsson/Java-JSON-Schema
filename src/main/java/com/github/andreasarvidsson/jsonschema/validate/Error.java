package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class Error {

    public final String message;
    public final String property;
    public final String name;
    public final Object argument;
    private final JsonSchema schema;
    public final Object instance;

    public Error(
            final String property, final String name, final Object argument,
            final String message, final JsonSchema schema, final Object instance) {
        this.property = property;
        this.name = name;
        this.argument = argument;
        this.message = message;
        this.schema = schema;
        this.instance = instance;
    }

    public Error(
            final String property, final String name, final Object argument,
            final String message) {
        this(property, name, argument, message, null, null);
    }

    public Map<String, Object> getSchema() {
        return schema != null ? JsonSchemaUtil.toMap(schema) : null;
    }

}
