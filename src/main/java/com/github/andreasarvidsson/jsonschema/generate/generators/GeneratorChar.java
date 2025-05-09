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
public class GeneratorChar extends GeneratorBase {

    public GeneratorChar() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.DEPRECATED,
                JsonSchemaField.CONST));
    }

    @Override
    public ObjectNode parseClass(final Class<?> type) {
        final ObjectNode result = super.parseClass(type);
        result.put(JsonSchemaField.MIN_LENGTH.toString(), 1);
        result.put(JsonSchemaField.MAX_LENGTH.toString(), 1);
        return result;
    }

    @Override
    public void addFields(final Class<?> type, final ObjectNode target, final JsonSchema jsonSchema) {
        super.addFields(type, target, jsonSchema);
        JsonSchemaUtil.setString(type, target, JsonSchemaField.CONST, jsonSchema.constant());
    }

}
