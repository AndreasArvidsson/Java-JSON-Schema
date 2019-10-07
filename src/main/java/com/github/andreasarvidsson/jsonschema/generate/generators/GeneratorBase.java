package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class GeneratorBase implements Generator {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonType jsonType;
    private final Set<String> schemaFields = new HashSet();

    public GeneratorBase(final Collection<JsonSchemaField> schemaFields) {
        this(null, schemaFields);
    }

    public GeneratorBase(final JsonType jsonType, final Collection<JsonSchemaField> schemaFields) {
        this.jsonType = jsonType;
        schemaFields.forEach(f -> {
            this.schemaFields.add(f.toString());
        });
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = MAPPER.createObjectNode();
        if (jsonType != null) {
            result.put(JsonSchemaField.Disabled.TYPE.toString(), jsonType.toString());
        }
        addFields(type, result);
        return result;
    }

    @Override
    public void addFields(final Class type, final ObjectNode target) {
        final JsonSchema[] anotations = (JsonSchema[]) type.getAnnotationsByType(JsonSchema.class);
        for (final JsonSchema jsonSchema : anotations) {
            if (jsonSchema.combining() != JsonSchema.Combining.NONE) {
                throw new RuntimeException(String.format(
                        "Schema combinings(anyOf, oneOf, allOf) is not allowed on class level. '%s'",
                        type.getTypeName())
                );
            }
            addFields(type, target, jsonSchema);
        }
    }

    @Override
    public void addFields(final Class type, final ObjectNode target, final JsonSchema jsonSchema) {
        JsonSchemaUtil.validateAllowedFields(type, schemaFields, jsonSchema);
        JsonSchemaUtil.setString(type, target, JsonSchemaField.TITLE, jsonSchema.title());
        JsonSchemaUtil.setString(type, target, JsonSchemaField.DESCRIPTION, jsonSchema.description());
    }

}
