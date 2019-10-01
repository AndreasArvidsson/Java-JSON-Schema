package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class BoolTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";

    @Test
    public void testBoolPrim() {
        AssertJson.assertEquals(
                new JsonBuilder().setType(JsonType.BOOLEAN).build(),
                gen.create(boolean.class)
        );
    }

    @Test
    public void testBoolClass() {
        AssertJson.assertEquals(
                new JsonBuilder().setType(JsonType.BOOLEAN).build(),
                gen.create(Boolean.class)
        );
    }

    @Test
    public void testBoolAnotations() {
        final JsonNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.BOOLEAN)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.create(BoolClass.class)
        );
    }

    class BoolClass {

        @JsonSchema(
                title = title,
                description = desc
        )
        Boolean value;
    }

}
