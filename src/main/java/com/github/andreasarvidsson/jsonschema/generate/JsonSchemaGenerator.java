package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.parsers.Parsers;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.github.andreasarvidsson.jsonschema.generate.parsers.InterfaceParser;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class JsonSchemaGenerator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private URI schemaField = URI.create("http://json-schema.org/draft-06/schema#");
    private boolean autoRangeNumbes = true;
    private final Map<Class, InterfaceParser> customParsers = new HashMap();

    public JsonSchemaGenerator hideSchemaField() {
        schemaField = null;
        return this;
    }

    public JsonSchemaGenerator setSchemaField(final URI schemaField) {
        this.schemaField = schemaField;
        return this;
    }

    public JsonSchemaGenerator disableAutoRangeNumbers() {
        autoRangeNumbes = false;
        return this;
    }

    public JsonSchemaGenerator addCustomParser(final Class type, final InterfaceParser parser) {
        customParsers.put(type, parser);
        return this;
    }

    public JsonNode create(final Class type) {
        final ObjectNode schemaNode = MAPPER.createObjectNode();
        final ClassDefinitions classDefinitions = new ClassDefinitions();
        final Parsers parsers = new Parsers(autoRangeNumbes, customParsers, classDefinitions);

        if (schemaField != null) {
            schemaNode.put("$schema", schemaField.toString());
        }

        //Parse root class
        final ObjectNode classNode = parsers.parseClass(type);

        //Update node with definitions;
        classDefinitions.update(schemaNode);

        //Add root class;
        schemaNode.setAll(classNode);

        return schemaNode;
    }

}
