package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserArray extends ParserBase {

    private final Parsers parsers;

    public ParserArray(final Parsers parsers) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.parsers = parsers;
    }

    public ObjectNode parseArray(final Field field) {
        final ObjectNode result = super.parseClass(field.getType());
        final Class valueType = field.getType().getComponentType();
        result.set("items", parsers.parseClass(valueType));
        return result;
    }

}
