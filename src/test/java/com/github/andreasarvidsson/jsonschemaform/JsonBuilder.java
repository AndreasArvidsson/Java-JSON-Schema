package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ObjectNode res, properties, patternProperties;
    private final ArrayNode required;
    private JsonNode items = null;
    private Boolean additionalProperties = null;

    public JsonBuilder() {
        res = MAPPER.createObjectNode();
        properties = MAPPER.createObjectNode();
        patternProperties = MAPPER.createObjectNode();
        required = MAPPER.createArrayNode();
    }

    public JsonBuilder setType(final JsonType type) {
        res.put("type", type.toString());
        return this;
    }

    public JsonBuilder setTypeNull(final JsonType type) {
        res.put("type", String.format("%s, null", type.toString()));
        return this;
    }

    public JsonBuilder setAdditionalProps(final boolean value) {
        additionalProperties = value;
        return this;
    }

    public JsonBuilder addField(final String field, final Object value) {
        res.putPOJO(field, value);
        return this;
    }

    public JsonBuilder addField(final JsonSchemaField field, final Object value) {
        addField(field.toString(), value);
        return this;
    }

    public JsonBuilder addRequired(final String value) {
        required.add(value);
        return this;
    }

    public JsonBuilder setItems(final JsonNode items) {
        this.items = items;
        return this;
    }

    public JsonBuilder addRequired(final Collection< String> required) {
        required.forEach(r -> addRequired(r));
        return this;
    }

    public JsonBuilder addProperty(final String key, final JsonNode value) {
        properties.set(key, value);
        return this;
    }

    public JsonBuilder addPatternProperty(final String key, final JsonNode value) {
        patternProperties.set(key, value);
        return this;
    }

    public ObjectNode build() {
        if (additionalProperties != null) {
            res.put("additionalProperties", additionalProperties);
        }
        if (required.size() > 0) {
            res.set("required", required);
        }
        if (properties.size() > 0) {
            res.set("properties", properties);
        }
        if (patternProperties.size() > 0) {
            res.set("patternProperties", patternProperties);
        }
        if (items != null) {
            res.set("items", items);
        }
        return res;
    }

}
