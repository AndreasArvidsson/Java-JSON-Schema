package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.UUID;
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
    private final String format = "any";
    private final String constant = "Hello";
    private final int min = 2;
    private final int max = 3;

    @Test
    public void testString() {
        AssertJson.assertEquals(
                getStringExpected(),
                gen.generate(String.class));
    }

    @Test
    public void testCharSeq() {
        AssertJson.assertEquals(
                getStringExpected(),
                gen.generate(CharSequence.class));
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
                        .addField(JsonSchemaField.DEPRECATED, true)
                        .addField(JsonSchemaField.MIN_LENGTH, min)
                        .addField(JsonSchemaField.MAX_LENGTH, max)
                        .addField(JsonSchemaField.PATTERN, pattern)
                        .addField(JsonSchemaField.FORMAT, format)
                        .addField(JsonSchemaField.CONST, constant)
                        .build())
                .build();
        AssertJson.assertEquals(
                expected,
                gen.generate(StringClass.class));
    }

    @Test
    public void testUUID() {
        AssertJson.assertEquals(
                getStringExpected(),
                gen.generate(UUID.class));
    }

    private JsonNode getStringExpected() {
        return new JsonBuilder()
                .setType(JsonType.STRING)
                .build();
    }

    class StringClass {

        @JsonSchema(title = title, description = desc, deprecated = true, minLength = min, maxLength = max, pattern = pattern, format = format, constant = constant)
        public String value;
    }

}
