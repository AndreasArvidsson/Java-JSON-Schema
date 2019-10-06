package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorMap extends GeneratorBase implements GeneratorCollectionInterface {

    private final Generators generators;

    public GeneratorMap(final Generators generators) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_PROPERTIES,
                JsonSchemaField.MAX_PROPERTIES
        ));
        this.generators = generators;
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        final ObjectNode result = super.parseClass(type);
        final ObjectNode patternProperties = MAPPER.createObjectNode();
        patternProperties.set(JsonSchemaField.Disabled.ANY_MATCH.toString(), generators.parseClass(valueType));
        result.set(JsonSchemaField.Disabled.PATTERN_PROPERTIES.toString(), patternProperties);
        return result;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
