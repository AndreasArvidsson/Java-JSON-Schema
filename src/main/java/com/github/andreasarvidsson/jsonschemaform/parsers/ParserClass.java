package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserClass extends ParserBase {

    private final Parsers parsers;
    private final ParserMap parserMap;
    private final ParserArray parserArray;
    private final ParserSet parserSet;
    private final ParserCollection parserCollection;

    public ParserClass(final Parsers parsers) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
        this.parserMap = new ParserMap(parsers);
        this.parserArray = new ParserArray(parsers);
        this.parserSet = new ParserSet(parsers);
        this.parserCollection = new ParserCollection(parsers);
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

    public Set<JsonSchemaField> getAllowedSchemaFields(final Field field) {
        final Class type = field.getType();
        if (isArray(type)) {
            return parserArray.getAllowedSchemaFields();
        }
        if (isMap(type)) {
            return parserMap.getAllowedSchemaFields();
        }
        if (isSet(type)) {
            return parserSet.getAllowedSchemaFields();
        }
        if (isCollection(type)) {
            return parserCollection.getAllowedSchemaFields();
        }
        return super.getAllowedSchemaFields();
    }

    private ObjectNode parseClassField(final Field field) {
        final Class type = field.getType();
        if (isArray(type)) {
            return parserArray.parseArray(field);
        }
        if (isMap(type)) {
            return parserMap.parseMap(field);
        }
        if (isSet(type)) {
            return parserSet.parseSet(field);
        }
        //Catch all collections that are not map or set. Should only be lists.
        if (isCollection(type)) {
            return parserCollection.parseCollection(field);
        }
        return parsers.parseClass(type);
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

            final ObjectNode fieldNode = parseClassField(field);
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
        final Set<JsonSchemaField> allowedFields = parsers.getAllowedSchemaFields(field);
        JsonSchemaUtil.addFields(field.getType(), target, field, allowedFields);
    }

    private void setIsNullable(final Class type, final ObjectNode node, final boolean isNullable) {
        String typeStr = node.has("type") ? node.get("type").asText() : parsers.getDefType(type);
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

    private boolean isArray(final Class type) {
        return type.isArray();
    }

    private boolean isCollection(final Class type) {
        return Collection.class.isAssignableFrom(type);
    }

    private boolean isSet(final Class type) {
        return Set.class.isAssignableFrom(type);
    }

    private boolean isMap(final Class type) {
        return Map.class.isAssignableFrom(type);
    }

}
