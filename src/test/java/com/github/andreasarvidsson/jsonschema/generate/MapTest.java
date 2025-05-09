package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class MapTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 1;
    private final int max = 2;

    @Test
    public void testMapRoot() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .addPatternProperty("^.*$", new JsonBuilder().build())
                        .build(),
                gen.generate(Map.class));
    }

    @Test
    public void testMapDefault() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.generate(MapDefault.class));
    }

    @Test
    public void testMapOfObjects() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.generate(MapOfObjects.class));
    }

    @Test
    public void testMapOfBools() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.generate(MapOfBools.class));
    }

    @Test
    public void testMapOfMaps() {
        AssertJson.assertEquals(
                getExpectedMapOfMaps(new JsonBuilder().build()),
                gen.generate(MapOfMaps.class));
    }

    @Test
    public void testMapOfMapsOfObjects() {
        AssertJson.assertEquals(
                getExpectedMapOfMaps(new JsonBuilder()
                        .build()),
                gen.generate(MapOfMapsOfObjects.class));
    }

    @Test
    public void testMapOfMapsOfBools() {
        AssertJson.assertEquals(
                getExpectedMapOfMaps(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.generate(MapOfMapsOfBools.class));
    }

    private JsonNode getExpected(final JsonNode property) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.MIN_PROPERTIES, min)
                        .addField(JsonSchemaField.MAX_PROPERTIES, max)
                        .addPatternProperty(JsonSchemaField.Disabled.ANY_MATCH.toString(), property)
                        .build())
                .build();
    }

    private JsonNode getExpectedMapOfMaps(final JsonNode property) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .addPatternProperty(JsonSchemaField.Disabled.ANY_MATCH.toString(), new JsonBuilder()
                                .setType(JsonType.OBJECT)
                                .addPatternProperty(JsonSchemaField.Disabled.ANY_MATCH.toString(), property)
                                .build())
                        .build())
                .build();
    }

    class MapDefault {

        @SuppressWarnings("rawtypes")
        @JsonSchema(title = title, description = desc, minProperties = min, maxProperties = max)
        public Map value;

    }

    class MapOfObjects {

        @JsonSchema(title = title, description = desc, minProperties = min, maxProperties = max)
        public Map<String, Object> value;

    }

    class MapOfBools {

        @JsonSchema(title = title, description = desc, minProperties = min, maxProperties = max)
        public Map<String, Boolean> value;

    }

    class MapOfMaps {

        @SuppressWarnings("rawtypes")
        public Map<String, Map> value;

    }

    class MapOfMapsOfObjects {

        public Map<String, Map<String, Object>> value;

    }

    class MapOfMapsOfBools {

        public Map<String, Map<String, Boolean>> value;

    }

}
