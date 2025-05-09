package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorNumber extends GeneratorBase {

    public final static List<JsonSchemaField> FIELDS = Arrays.asList(
            JsonSchemaField.TITLE,
            JsonSchemaField.DESCRIPTION,
            JsonSchemaField.DEPRECATED,
            JsonSchemaField.MINIMUM,
            JsonSchemaField.MAXIMUM,
            JsonSchemaField.EXCLUSIVE_MINIMUM,
            JsonSchemaField.EXCLUSIVE_MAXIMUM,
            JsonSchemaField.MULTIPLE_OF,
            JsonSchemaField.CONST);

    public GeneratorNumber() {
        super(JsonType.NUMBER, FIELDS);
    }

    @Override
    public void addFields(final Class<?> type, final ObjectNode target, final JsonSchema jsonSchema) {
        super.addFields(type, target, jsonSchema);
        JsonSchemaUtil.setNumbers(type, target,
                JsonSchemaField.MINIMUM, jsonSchema.minimum(),
                JsonSchemaField.MAXIMUM, jsonSchema.maximum());
        JsonSchemaUtil.setNumbers(type, target,
                JsonSchemaField.EXCLUSIVE_MINIMUM, jsonSchema.exclusiveMinimum(),
                JsonSchemaField.EXCLUSIVE_MAXIMUM, jsonSchema.exclusiveMaximum());
        JsonSchemaUtil.setNumber(type, target, JsonSchemaField.MULTIPLE_OF, jsonSchema.multipleOf());
        JsonSchemaUtil.setNumber(type, target, JsonSchemaField.CONST, jsonSchema.constant());
    }

}
