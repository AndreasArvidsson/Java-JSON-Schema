package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class CollectionTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 1;
    private final int max = 2;

    @Test
    public void testListBool() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setTypeNull(JsonType.BOOLEAN)
                        .build()),
                gen.create(ListBool.class)
        );
    }

    @Test
    public void testListObject() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(ListObject.class)
        );
    }

    @Test
    public void testListDefault() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(ListDefault.class)
        );
    }

    @Test
    public void testArrayListRoot() {
        AssertJson.assertEquals(
                new JsonBuilder()
                .setType(JsonType.ARRAY)
                .build(),
                new JsonSchemaGenerator().hideSchemaField().create(ArrayList.class)
        );
    }


    private JsonNode getExpected(final JsonNode items) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.ARRAY)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.MIN_ITEMS, min)
                        .addField(JsonSchemaField.MAX_ITEMS, max)
                        .setItems(items)
                        .build()
                )
                .build();
    }

    class ListBool {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        List<Boolean> value;

    }

    class ListObject {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        List<Object> value;

    }

    class ListDefault {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        List value;

    }

}
