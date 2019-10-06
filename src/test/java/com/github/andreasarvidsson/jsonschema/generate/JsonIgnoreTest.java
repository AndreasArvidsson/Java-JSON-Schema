package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class JsonIgnoreTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testJsonIgnore() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value1",
                        new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build()
                )
                .build(),
                gen.create(JsonIgnoreClass.class)
        );
    }

    class JsonIgnoreClass {

        String value1;

        @JsonIgnore
        String value2;

    }

}
