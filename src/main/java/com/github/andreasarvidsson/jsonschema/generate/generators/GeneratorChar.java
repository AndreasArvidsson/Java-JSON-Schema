package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorChar extends GeneratorBase {

    public GeneratorChar() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = super.parseClass(type);
        result.put(JsonSchemaField.MIN_LENGTH.toString(), 1);
        result.put(JsonSchemaField.MAX_LENGTH.toString(), 1);
        return result;
    }

}
