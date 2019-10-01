package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class StringTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final String pattern = "\\s";
    private final int min = 2;
    private final int max = 3;

    @Test
    public void testString() {
        AssertJson.assertEquals(
                getStringExpected(),
                gen.create(String.class)
        );
    }

    @Test
    public void testCharSeq() {
        AssertJson.assertEquals(
                getStringExpected(),
                gen.create(CharSequence.class)
        );
    }

    @Test
    public void testChar() {
        AssertJson.assertEquals(
                getCharExpected(),
                gen.create(char.class)
        );
    }

    @Test
    public void testCharacter() {
        AssertJson.assertEquals(
                getCharExpected(),
                gen.create(Character.class)
        );
    }

    @Test
    public void testStringAnotations() {
        final JsonNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.STRING)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.MIN_LENGTH, min)
                        .addField(JsonSchemaField.MAX_LENGTH, max)
                        .addField(JsonSchemaField.PATTERN, pattern)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.create(StringClass.class)
        );
    }

    private JsonNode getStringExpected() {
        return new JsonBuilder()
                .setType(JsonType.STRING)
                .build();
    }

    private JsonNode getCharExpected() {
        return new JsonBuilder()
                .setType(JsonType.STRING)
                .addField(JsonSchemaField.MIN_LENGTH, 1)
                .addField(JsonSchemaField.MAX_LENGTH, 1)
                .build();
    }

    class StringClass {

        @JsonSchema(
                title = title,
                description = desc,
                minLength = min,
                maxLength = max,
                pattern = pattern
        )
        String value;
    }

}
