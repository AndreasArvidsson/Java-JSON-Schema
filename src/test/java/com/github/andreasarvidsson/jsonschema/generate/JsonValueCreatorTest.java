package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import javax.validation.constraints.Pattern;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonValueCreatorTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testNormalClass() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build()
                        )
                        .build(),
                gen.create(NormalClass.class)
        );
    }

    @Test
    public void testJsonValueClass() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build(),
                gen.create(JsonValueClass.class)
        );
    }

    @Test
    public void testJsonCreatorClass() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.STRING)
                        .addField(JsonSchemaField.PATTERN, "\\s")
                        .build(),
                gen.create(JsonCreatorClass.class)
        );
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

    class JsonCreatorClass {

        public String value;

        @JsonCreator
        @Pattern(regexp = "\\s")
        public JsonCreatorClass(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

    }

}
