package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserClass extends ParserBase {

    private final Parsers parsers;

    public ParserClass(final Parsers parsers) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parse(final Class type) {
        return parseInner(type, false);
    }

    @Override
    public ObjectNode parseRoot(final Class type) {
        return parseInner(type, true);
    }

    private ObjectNode parseInner(final Class type, final boolean isRoot) {
        ObjectNode result;
        if (isRoot) {
            result = super.parseRoot(type);
        }
        else {
            result = super.parse(type);
        }

        result.put("additionalProperties", ReflectionUtil.hasAnyGetterAndAnySetter(type));
        final ObjectNode properties = MAPPER.createObjectNode();
        final ArrayNode required = MAPPER.createArrayNode();
        parseClassFields(properties, required, type);
        if (required.size() > 0) {
            result.set("required", required);
        }
        result.set("properties", properties);

        return result;
    }

    private void parseClassFields(final ObjectNode properties, final ArrayNode required, final Class type) {
        //Parse super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            parseClassFields(properties, required, superType);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String name = ReflectionUtil.getFieldName(field);
            final ObjectNode fieldNode = parsers.parse(field.getType());
            if (JsonSchemaUtil.isRequired(field)) {
                required.add(name);
            }

            //Add field anotations
            JsonSchemaUtil.addFields(fieldNode, field, schemaFields);

            properties.set(name, fieldNode);
        }
    }

}
