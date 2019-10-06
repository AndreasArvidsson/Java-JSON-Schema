package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.generate.generators.GeneratorString;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class CustomGeneratorTest {

    @Test
    public void testExistingGenerator() {
        final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField()
                .addCustomGenerator(CustomClass.class, new GeneratorString());
        AssertJson.assertEquals(
                getExpected(),
                gen.create(CustomClass.class)
        );
    }

    @Test
    public void testCustomGenerator() {
        final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField()
                .addCustomGenerator(CustomClass.class, new CustomGenerator());
        AssertJson.assertEquals(
                getExpected(),
                gen.create(CustomClass.class)
        );
    }

    private JsonNode getExpected() {
        return new JsonBuilder()
                .setType(JsonType.STRING)
                .build();
    }

    class CustomGenerator extends GeneratorString {

    }

    class CustomClass {

        public String value;

    }

}
