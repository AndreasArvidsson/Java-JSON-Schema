package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.generate.parsers.ParserString;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class CustomParserTest {

    @Test
    public void testExistingParser() {
        final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField()
                .addCustomParser(CustomClass.class, new ParserString());
        AssertJson.assertEquals(
                getExpected(),
                gen.create(CustomClass.class)
        );
    }

    @Test
    public void testCustomParser() {
        final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField()
                .addCustomParser(CustomClass.class, new CustomParser());
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

    class CustomParser extends ParserString {

    }

    class CustomClass {

        String value;

    }

}
