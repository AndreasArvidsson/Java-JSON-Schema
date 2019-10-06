package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.generators.Generators;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import com.github.andreasarvidsson.jsonschema.generate.generators.Generator;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonSchemaGenerator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private URI schemaField = URI.create("http://json-schema.org/draft-06/schema#");
    private boolean autoRangeNumbes = true;
    private final Map<Class, Generator> customGenerators = new HashMap();

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

    public JsonSchemaGenerator addCustomGenerator(final Class type, final Generator generator) {
        customGenerators.put(type, generator);
        return this;
    }

    public JsonNode generate(final Class type) {
        final ObjectNode schemaNode = MAPPER.createObjectNode();
        final ClassDefinitions classDefinitions = new ClassDefinitions();
        final Generators generators = new Generators(autoRangeNumbes, customGenerators, classDefinitions);

        if (schemaField != null) {
            schemaNode.put(JsonSchemaField.Disabled.SCHEMA.toString(), schemaField.toString());
        }

        //Generate root class
        final ObjectNode classNode = generators.parseClass(type);

        //Update node with definitions;
        classDefinitions.update(schemaNode);

        //Add root class;
        schemaNode.setAll(classNode);

        return schemaNode;
    }

}
