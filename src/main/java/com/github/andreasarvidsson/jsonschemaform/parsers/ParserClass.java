package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
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
        final ObjectNode dependencies = MAPPER.createObjectNode();
        final ArrayNode required = MAPPER.createArrayNode();
        final Set<String> fieldNames = new HashSet();
        parseClassFields(properties, dependencies, required, fieldNames, type);
        if (required.size() > 0) {
            result.set("required", required);
        }
        if (dependencies.size() > 0) {
            result.set("dependencies", dependencies);
            validateDependencies(type, fieldNames, dependencies);
        }
        result.set("properties", properties);
        return result;
    }

    private void parseClassFields(
            final ObjectNode properties, final ObjectNode dependencies,
            final ArrayNode required, final Set<String> fieldNames, final Class type) {
        //Parse super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            parseClassFields(properties, dependencies, required, fieldNames, superType);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String name = ReflectionUtil.getFieldName(field);
            fieldNames.add(name);

            final ObjectNode fieldNode = parsers.parseClassField(field);
            if (JsonSchemaUtil.isRequired(field)) {
                required.add(name);
            }

            //Add field anotations
            appendSchemaFields(field, fieldNode);

            properties.set(name, fieldNode);

            //Apply dependencies
            final String[] fieldDependencies = JsonSchemaUtil.getDependencies(field);
            if (fieldDependencies.length > 0) {
                final ArrayNode dependenciesNode = MAPPER.createArrayNode();
                for (final String fieldName : fieldDependencies) {
                    dependenciesNode.add(fieldName);
                }
                dependencies.set(name, dependenciesNode);
            }
        }
    }

    private void validateDependencies(final Class type, final Set<String> fieldNames, final ObjectNode dependencies) {
        for (final JsonNode fieldDependencies : dependencies) {
            for (final JsonNode fieldName : fieldDependencies) {
                if (!fieldNames.contains(fieldName.asText())) {
                    throw new RuntimeException(String.format(
                            "Dependency on missing field '%s' not possible for class '%s'",
                            fieldName.asText(), type.getTypeName())
                    );
                }
            }
        }
    }

    private void appendSchemaFields(final Field field, final ObjectNode target) {
        final Set<JsonSchemaField> allowedFields = parsers.getAllowedSchemaFields(field.getType());
        JsonSchemaUtil.addFields(field.getType(), target, field, allowedFields);
    }

}
