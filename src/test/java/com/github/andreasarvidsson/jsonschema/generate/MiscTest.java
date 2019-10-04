package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class MiscTest {

    @Test
    public void testWithSchema() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .addField("$schema", "http://json-schema.org/draft-06/schema#")
                        .setType(JsonType.STRING)
                        .build(),
                new JsonSchemaGenerator().create(String.class)
        );
    }

    @Test
    public void testWithOtherSchema() {
        try {
            final URI newURI = new URI("http://json-schema.org/draft-06/newSchema#");
            AssertJson.assertEquals(
                    new JsonBuilder()
                            .addField("$schema", newURI.toString())
                            .setType(JsonType.STRING)
                            .build(),
                    new JsonSchemaGenerator().setSchemaField(newURI).create(String.class)
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
                new JsonSchemaGenerator().hideSchemaField().create(String.class)
        );
    }

    @Test
    public void testObjectRoot() {
        AssertJson.assertEquals(
                new JsonBuilder().build(),
                new JsonSchemaGenerator().hideSchemaField().create(Object.class)
        );
    }

}
