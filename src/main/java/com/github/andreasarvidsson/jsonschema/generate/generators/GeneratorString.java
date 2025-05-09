package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorString extends GeneratorBase {

    public GeneratorString() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.DEPRECATED,
                JsonSchemaField.MIN_LENGTH,
                JsonSchemaField.MAX_LENGTH,
                JsonSchemaField.PATTERN,
                JsonSchemaField.FORMAT,
                JsonSchemaField.CONST));
    }

    @Override
    public void addFields(final Class<?> type, final ObjectNode target, final JsonSchema jsonSchema) {
        super.addFields(type, target, jsonSchema);
        JsonSchemaUtil.setIntegers(type, target,
                JsonSchemaField.MIN_LENGTH, jsonSchema.minLength(), 0, Integer.MAX_VALUE,
                JsonSchemaField.MAX_LENGTH, jsonSchema.maxLength(), 1, Integer.MAX_VALUE);
        JsonSchemaUtil.setString(type, target, JsonSchemaField.PATTERN, jsonSchema.pattern());
        JsonSchemaUtil.setString(type, target, JsonSchemaField.FORMAT, jsonSchema.format());
        JsonSchemaUtil.setString(type, target, JsonSchemaField.CONST, jsonSchema.constant());
    }

}
