package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import java.util.Map;

public class Error {

    public final String property;
    public final String name;
    public final Object argument;
    public final String message;
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

    public Map<String, Object> getSchema() {
        return JsonSchemaUtil.toMap(schema);
    }

}
