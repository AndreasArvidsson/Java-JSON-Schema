package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ArrayTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 1;
    private final int max = 2;

    @Test
    public void testArrayBoolPrim() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.generate(ArrayBoolPrim.class)
        );
    }

    @Test
    public void testArrayBoolClass() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.generate(ArrayBoolClass.class)
        );
    }

    @Test
    public void testArrayObject() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.generate(ArrayObject.class)
        );
    }

    @Test
    public void testArrayBoolRoot() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.ARRAY)
                .setItems(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()
                )
                .build(),
                gen.generate(Boolean[].class)
        );
    }

    private JsonNode getExpected(final JsonNode items) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.ARRAY)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.MIN_ITEMS, min)
                        .addField(JsonSchemaField.MAX_ITEMS, max)
                        .setItems(items)
                        .build()
                )
                .build();
    }

    class ArrayBoolPrim {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public boolean[] value;

    }

    class ArrayBoolClass {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public Boolean[] value;

    }

    class ArrayObject {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public Object[] value;

    }

}
