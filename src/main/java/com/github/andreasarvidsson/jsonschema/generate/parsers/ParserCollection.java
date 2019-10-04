package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserCollection extends ParserBase implements InterfaceParserCollection {

    private final Parsers parsers;

    public ParserCollection(final Parsers parsers) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        final ObjectNode result = super.parseClass(type);
        result.set("items", parsers.parseClass(valueType));
        return result;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
