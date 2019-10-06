package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class MiscTest {

    @Test
    public void testWithSchema() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .addField(JsonSchemaField.Disabled.SCHEMA, "http://json-schema.org/draft-06/schema#")
                        .setType(JsonType.STRING)
                        .build(),
                new JsonSchemaGenerator().generate(String.class)
        );
    }

    @Test
    public void testWithOtherSchema() {
        try {
            final URI newURI = new URI("http://json-schema.org/draft-06/newSchema#");
            AssertJson.assertEquals(
                    new JsonBuilder()
                            .addField(JsonSchemaField.Disabled.SCHEMA, newURI.toString())
                            .setType(JsonType.STRING)
                            .build(),
                    new JsonSchemaGenerator().setSchemaField(newURI).generate(String.class)
            );
        }
        catch (final URISyntaxException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    public void testWithoutSchema() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build(),
                new JsonSchemaGenerator().hideSchemaField().generate(String.class)
        );
    }

    @Test
    public void testObjectRoot() {
        AssertJson.assertEquals(
                new JsonBuilder().build(),
                new JsonSchemaGenerator().hideSchemaField().generate(Object.class)
        );
    }

}
