package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ObjectNode res, properties;

    public JsonBuilder() {
        res = MAPPER.createObjectNode();
        properties = MAPPER.createObjectNode();
        res.put("$schema", "http://json-schema.org/draft-06/schema#");
        res.put("type", "object");
    }

    public void addField(final String key, final String value) {
        res.put(key, value);
    }

    public void addProperty(final String key, final JsonNode value) {
        properties.set(key, value);
    }

    public JsonNode build() {
        res.put("additionalProperties", false);
        res.set("properties", properties);
        return res;
    }

}
