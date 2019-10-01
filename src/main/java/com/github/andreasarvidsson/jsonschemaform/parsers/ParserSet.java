package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserSet extends ParserBase {

    private final Parsers parsers;

    public ParserSet(final Parsers parsers) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parseClassField(final Field field) {
        final ObjectNode result = super.parseClass(field.getType());
        final Class valueType = ReflectionUtil.getGenericValueType(field);
        result.put("uniqueItems", true);
        result.set("items", parsers.parseClass(valueType));
        return result;
    }

}
