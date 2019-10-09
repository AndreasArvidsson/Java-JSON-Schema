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
public class BoolTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final String constant = "true";

    @Test
    public void testBoolPrim() {
        AssertJson.assertEquals(
                new JsonBuilder().setType(JsonType.BOOLEAN).build(),
                gen.generate(boolean.class)
        );
    }

    @Test
    public void testBoolClass() {
        AssertJson.assertEquals(
                new JsonBuilder().setType(JsonType.BOOLEAN).build(),
                gen.generate(Boolean.class)
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
                        .addField(JsonSchemaField.CONST, Boolean.parseBoolean(constant))
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.generate(BoolClass.class)
        );
    }

    class BoolClass {

        @JsonSchema(
                title = title,
                description = desc,
                constant = constant
        )
        public Boolean value;
    }

}
