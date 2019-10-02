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
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", getSimpleExpected(true))
                .build(),
                gen.create(SimpleEnumClass.class)
        );
    }

    @Test
    public void testSimpleEnumRequiredClass() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addRequired("value")
                .addProperty("value", getSimpleExpected(false))
                .build(),
                gen.create(SimpleEnumRequiredClass.class)
        );
    }

    @Test
    public void testDescriptiveEnum() {
        AssertJson.assertEquals(
                getDescriptiveExpected(false),
                gen.create(DescriptiveEnum.class)
        );
    }

    @Test
    public void testDescriptiveEnumClass() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", getDescriptiveExpected(true))
                .build(),
                gen.create(DescriptiveEnumClass.class)
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

    private JsonNode getDescriptiveExpected(final boolean includeNull) {
        final JsonBuilder b = new JsonBuilder()
                .addField(JsonSchemaField.TITLE, title)
                .addField(JsonSchemaField.DESCRIPTION, desc);
        if (includeNull) {
            b.addOneOf(new JsonBuilder()
                    .addField("const", null)
                    .build()
            );
        }
        return b.addOneOf(new JsonBuilder()
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
                .build();
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

    class SimpleEnumRequiredClass {

        @JsonSchema(
                required = true
        )
        SimpleEnum value;
    }

    class DescriptiveEnumClass {

        DescriptiveEnum value;
    }

}
