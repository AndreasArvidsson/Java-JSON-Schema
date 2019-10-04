package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorArray extends GeneratorBase {

    private final Generators parsers;

    public GeneratorArray(final Generators parsers) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = super.parseClass(type);
        final Class valueType = type.getComponentType();
        result.set(JsonSchemaField.Disabled.ITEMS.toString(), parsers.parseClass(valueType));
        return result;
    }

}
