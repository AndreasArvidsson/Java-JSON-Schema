package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
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
    public void testMapBool() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.create(MapBoolClass.class)
        );
    }

    @Test
    public void testMapObject() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(MapObjectClass.class)
        );
    }

    @Test
    public void testMapDefault() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(MapDefaultClass.class)
        );
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
                        .addPatternProperty("^.*$", property)
                        .build()
                )
                .build();
    }

    class MapBoolClass {

        @JsonSchema(
                title = title,
                description = desc,
                minProperties = min,
                maxProperties = max
        )
        Map<String, Boolean> value;

    }

    class MapObjectClass {

        @JsonSchema(
                title = title,
                description = desc,
                minProperties = min,
                maxProperties = max
        )
        Map<String, Object> value;

    }

    class MapDefaultClass {

        @JsonSchema(
                title = title,
                description = desc,
                minProperties = min,
                maxProperties = max
        )
        Map value;

    }

}
