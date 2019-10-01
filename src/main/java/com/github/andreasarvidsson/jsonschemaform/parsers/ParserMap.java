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
public class ParserMap extends ParserBase {

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

    public ObjectNode parseMap(final Field field) {
        final ObjectNode result = super.parseClass(field.getType());
        final Class valueType = ReflectionUtil.getGenericValueType(field);
        final ObjectNode patternProperties = MAPPER.createObjectNode();
        patternProperties.set("^.*$", parsers.parseClass(valueType));
        result.set("patternProperties", patternProperties);
        return result;
    }

}
