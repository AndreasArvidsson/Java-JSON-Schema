package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class EnumTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";

    @Test
    public void testSimpleEnum() {
        AssertJson.assertEquals(
                getSimpleExpected(false),
                gen.create(SimpleEnum.class)
        );
    }

    @Test
    public void testSimpleEnumClass() {
        AssertJson.assertEquals(
                new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", getSimpleExpected(true))
                .build(),
                gen.create(SimpleEnumClass.class)
        );
    }

    private JsonNode getSimpleExpected(final boolean includeNull) {
        final JsonBuilder b = new JsonBuilder()
                .addField(JsonSchemaField.TITLE, title)
                .addField(JsonSchemaField.DESCRIPTION, desc);
        if (includeNull) {
            b.addEnum(null);
        }
        return b
                .addEnum("A")
                .addEnum("B")
                .addEnum("C")
                .build();
    }

    @Test
    public void testDescriptiveEnum() {
        AssertJson.assertEquals(
                new JsonBuilder()
                .addField(JsonSchemaField.TITLE, title)
                .addField(JsonSchemaField.DESCRIPTION, desc)
                .addOneOf(new JsonBuilder()
                        .addField("const", "A")
                        .addField(JsonSchemaField.TITLE, "a")
                        .addField(JsonSchemaField.DESCRIPTION, "A_desc")
                        .build()
                )
                .addOneOf(new JsonBuilder()
                        .addField("const", "B")
                        .addField(JsonSchemaField.TITLE, "b")
                        .addField(JsonSchemaField.DESCRIPTION, "B_desc")
                        .build()
                )
                .addOneOf(new JsonBuilder()
                        .addField("const", "C")
                        .addField(JsonSchemaField.TITLE, "c")
                        .addField(JsonSchemaField.DESCRIPTION, "C_desc")
                        .build()
                )
                .build(),
                gen.create(DescriptiveEnum.class)
        );
    }

    @JsonSchema(
            title = title,
            description = desc
    )
    enum SimpleEnum {

        A, B, C;
    }

    @JsonSchema(
            title = title,
            description = desc
    )
    public static enum DescriptiveEnum implements JsonSchemaEnum {

        A, B, C;

        @JsonValue
        public Object getVal() {
            return toString();
        }

        @Override
        public String getTitle() {
            return toString().toLowerCase();
        }

        @Override
        public String getDescription() {
            return toString() + "_desc";
        }
    }

    class SimpleEnumClass {

        SimpleEnum value;
    }

}
