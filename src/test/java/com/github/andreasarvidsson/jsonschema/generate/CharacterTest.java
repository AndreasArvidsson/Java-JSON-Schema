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
public class CharacterTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final String constant = "A";

    @Test
    public void testChar() {
        AssertJson.assertEquals(
                getCharExpected(),
                gen.generate(char.class)
        );
    }

    @Test
    public void testCharacter() {
        AssertJson.assertEquals(
                getCharExpected(),
                gen.generate(Character.class)
        );
    }

    @Test
    public void testStringAnotations() {
        final JsonNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.STRING)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.CONST, constant)
                        .addField(JsonSchemaField.MIN_LENGTH, 1)
                        .addField(JsonSchemaField.MAX_LENGTH, 1)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.generate(CharClass.class)
        );
    }

    private JsonNode getCharExpected() {
        return new JsonBuilder()
                .setType(JsonType.STRING)
                .addField(JsonSchemaField.MIN_LENGTH, 1)
                .addField(JsonSchemaField.MAX_LENGTH, 1)
                .build();
    }

    class CharClass {

        @JsonSchema(
                title = title,
                description = desc,
                constant = constant
        )
        public Character value;
    }

}
