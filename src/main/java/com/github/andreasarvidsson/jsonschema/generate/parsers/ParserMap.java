package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class ParserMap extends ParserBase implements InterfaceParserCollection {

    private final Parsers parsers;

    public ParserMap(final Parsers parsers) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_PROPERTIES,
                JsonSchemaField.MAX_PROPERTIES
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        final ObjectNode result = super.parseClass(type);
        final ObjectNode patternProperties = MAPPER.createObjectNode();
        patternProperties.set("^.*$", parsers.parseClass(valueType));
        result.set("patternProperties", patternProperties);
        return result;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
