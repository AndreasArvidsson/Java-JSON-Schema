package com.github.andreasarvidsson.jsonschema.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Collection;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ObjectNode res, definitions, properties, patternProperties, dependencies;
    private final ArrayNode required, enums, oneOf;
    private JsonNode items = null;
    private Boolean additionalProperties = null;
    private JsonType type;
    private String ref;

    public JsonBuilder() {
        res = MAPPER.createObjectNode();
        properties = MAPPER.createObjectNode();
        definitions = MAPPER.createObjectNode();
        patternProperties = MAPPER.createObjectNode();
        dependencies = MAPPER.createObjectNode();
        required = MAPPER.createArrayNode();
        enums = MAPPER.createArrayNode();
        oneOf = MAPPER.createArrayNode();
    }

    public JsonBuilder setType(final JsonType type) {
        this.type = type;
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

    public JsonBuilder addEnum(final Object value) {
        enums.addPOJO(value);
        return this;
    }

    public JsonBuilder addOneOf(final JsonNode value) {
        oneOf.add(value);
        return this;
    }

    public JsonBuilder addDefinition(final String field, final JsonNode value) {
        definitions.set(field, value);
        return this;
    }

    public JsonBuilder setItems(final JsonNode items) {
        this.items = items;
        return this;
    }

    public JsonBuilder addRequired(final Collection<String> required) {
        required.forEach(r -> addRequired(r));
        return this;
    }

    public JsonBuilder addDependencies(final String field, final String[] deps) {
        final ArrayNode depsNode = MAPPER.createArrayNode();
        for (final String dep : deps) {
            depsNode.add(dep);
        }
        dependencies.set(field, depsNode);
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

    public JsonBuilder setRef(final String ref) {
        this.ref = ref;
        return this;
    }

    public ObjectNode build() {
        if (definitions.size() > 0) {
            res.set("definitions", definitions);
        }
        if (ref != null) {
            res.put("$ref", String.format("#/definitions/%s", ref));
        }
        if (type != null) {
            res.put("type", type.toString());
        }
        if (additionalProperties != null) {
            res.put("additionalProperties", additionalProperties);
        }
        if (required.size() > 0) {
            res.set("required", required);
        }
        if (dependencies.size() > 0) {
            res.set("dependencies", dependencies);
        }
        if (properties.size() > 0) {
            res.set("properties", properties);
        }
        if (patternProperties.size() > 0) {
            res.set("patternProperties", patternProperties);
        }
        if (enums.size() > 0) {
            res.set("enum", enums);
        }
        if (oneOf.size() > 0) {
            res.set("oneOf", oneOf);
        }
        if (items != null) {
            res.set("items", items);
        }
        return res;
    }

}
