package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonValueTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testNormalClass() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .build(),
                gen.generate(NormalClass.class));
    }

    @Test
    public void testJsonValue() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build(),
                gen.generate(JsonValueClass.class));
    }

    @Test
    public void testJsonValueWithPattern() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.STRING)
                        .addField(JsonSchemaField.PATTERN, "\\s")
                        .build(),
                gen.generate(PatternClass.class));
    }

    class NormalClass {

        public String value;

    }

    class JsonValueClass {

        public String value;

        @JsonValue
        public String getValue() {
            return value;
        }

    }

    @JsonSchema(pattern = PatternClass.PATTERN)
    class PatternClass {

        private final static String PATTERN = "\\s";

        @JsonValue
        public String getValue() {
            return "";
        }

    }

}
