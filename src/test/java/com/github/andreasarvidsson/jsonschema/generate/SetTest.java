package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class SetTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 1;
    private final int max = 2;

    @Test
    public void testSetBool() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder()
                        .setType(JsonType.BOOLEAN)
                        .build()),
                gen.create(SetBool.class)
        );
    }

    @Test
    public void testSetObject() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(SetObject.class)
        );
    }

    @Test
    public void testSetDefault() {
        AssertJson.assertEquals(
                getExpected(new JsonBuilder().build()),
                gen.create(SetDefault.class)
        );
    }

    private JsonNode getExpected(final JsonNode items) {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.ARRAY)
                        .addField(JsonSchemaField.Disabled.UNIQUE_ITEMS, true)
                        .addField(JsonSchemaField.TITLE, title)
                        .addField(JsonSchemaField.DESCRIPTION, desc)
                        .addField(JsonSchemaField.MIN_ITEMS, min)
                        .addField(JsonSchemaField.MAX_ITEMS, max)
                        .setItems(items)
                        .build()
                )
                .build();
    }

    class SetBool {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        Set<Boolean> value;

    }

    class SetObject {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        Set<Object> value;

    }

    class SetDefault {

        @JsonSchema(
                title = title,
                description = desc,
                minItems = min,
                maxItems = max
        )
        Set value;

    }

}
