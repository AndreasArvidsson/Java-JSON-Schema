package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ListTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 1;
    private final int max = 2;

    @Test
    public void testArrayListRoot() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.ARRAY)
                        .setItems(new JsonBuilder().build())
                        .build(),
                gen.generate(ArrayList.class)
        );
    }

    @Test
    public void testListDefault() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.generate(ListDefault.class)
        );
    }

    @Test
    public void testListOfObjects() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.generate(ListOfObjects.class)
        );
    }

    @Test
    public void testListOfBools() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.generate(ListOfBools.class)
        );
    }

    @Test
    public void testListOfLists() {
        AssertJson.assertEquals(
                getExpectedArrayOfArrays(
                        new JsonBuilder().build()
                ),
                gen.generate(ListOfLists.class)
        );
    }

    @Test
    public void testListOfListsOfObjects() {
        AssertJson.assertEquals(
                getExpectedArrayOfArrays(
                        new JsonBuilder().build()
                ),
                gen.generate(ListOfListsOfObjects.class)
        );
    }

    @Test
    public void testListOfListsOfBools() {
        AssertJson.assertEquals(
                getExpectedArrayOfArrays(
                        new JsonBuilder()
                                .setType(JsonType.BOOLEAN)
                                .build()
                ),
                gen.generate(ListOfListsOfBools.class)
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

    private JsonNode getExpectedArrayOfArrays(final JsonNode items) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.ARRAY)
                        .setItems(new JsonBuilder()
                                .setType(JsonType.ARRAY)
                                .setItems(items)
                                .build())
                        .build()
                )
                .build();
    }

    class ListDefault {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public List value;

    }

    class ListOfObjects {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public List<Object> value;

    }

    class ListOfBools {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        public List<Boolean> value;

    }

    class ListOfLists {

        public List<List<Object>> value;

    }

    class ListOfListsOfObjects {

        public List<List<Object>> value;

    }

    class ListOfListsOfBools {

        public List<List<Boolean>> value;

    }

}
