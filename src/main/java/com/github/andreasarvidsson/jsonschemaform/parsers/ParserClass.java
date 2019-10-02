package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

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
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = super.parseClass(type);
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

            final ObjectNode fieldNode = parsers.parseClassField(field);
            final boolean isRequired = JsonSchemaUtil.isRequired(field);
            setIsNullable(field.getType(), fieldNode, !isRequired);
            if (isRequired) {
                required.add(name);
            }

            //Add field anotations
            appendSchemaFields(field, fieldNode);

            properties.set(name, fieldNode);
        }
    }

    private void appendSchemaFields(final Field field, final ObjectNode target) {
        final Set<JsonSchemaField> allowedFields = parsers.getAllowedSchemaFields(field.getType());
        JsonSchemaUtil.addFields(field.getType(), target, field, allowedFields);
    }

    private void setIsNullable(final Class type, final ObjectNode node, final boolean isNullable) {
        String typeStr = node.has("type") ? node.get("type").asText() : parsers.getDefType(type);
        if (typeStr == null) {
            return;
        }
        if (isNullable) {
            if (!typeStr.endsWith(", null")) {
                typeStr += ", null";
            }
        }
        else if (typeStr.endsWith(", null")) {
            typeStr = typeStr.replace(", null", "");
        }
        node.put("type", typeStr);
    }

}
