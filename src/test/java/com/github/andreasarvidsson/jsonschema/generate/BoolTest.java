package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
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
                        .setType(JsonType.BOOLEAN)
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
