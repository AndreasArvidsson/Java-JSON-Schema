package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorCollection extends GeneratorBase implements GeneratorCollectionInterface {

    private final Generators generators;

    public GeneratorCollection(final Generators generators) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.generators = generators;
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        final ObjectNode result = super.parseClass(type);
        result.set(JsonSchemaField.Disabled.ITEMS.toString(), generators.parseClass(valueType));
        return result;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
