package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaEnum;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
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
                getSimpleExpected(),
                gen.create(SimpleEnum.class)
        );
    }

    @Test
    public void testSimpleEnumClass() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", getSimpleExpected())
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
                .addProperty("value", getSimpleExpected())
                .build(),
                gen.create(SimpleEnumRequiredClass.class)
        );
    }

    @Test
    public void testDescriptiveEnum() {
        AssertJson.assertEquals(
                getDescriptiveExpected(),
                gen.create(DescriptiveEnum.class)
        );
    }

    @Test
    public void testDescriptiveEnumClass() {
        AssertJson.assertEquals(new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", getDescriptiveExpected())
                .build(),
                gen.create(DescriptiveEnumClass.class)
        );
    }

    @Test
    public void testDoubleEnumClass() {
        AssertJson.assertEquals(new JsonBuilder()
                .addDefinition(DescriptiveEnum.class.getSimpleName(), getDescriptiveExpected())
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value1", new JsonBuilder()
                        .setRef(DescriptiveEnum.class.getSimpleName())
                        .build()
                )
                .addProperty("value2", new JsonBuilder()
                        .setRef(DescriptiveEnum.class.getSimpleName())
                        .build()
                )
                .build(),
                gen.create(DoubleEnumClass.class)
        );
    }

    private JsonNode getSimpleExpected() {
        return new JsonBuilder()
                .addField(JsonSchemaField.TITLE, title)
                .addField(JsonSchemaField.DESCRIPTION, desc)
                .addEnum("A")
                .addEnum("B")
                .addEnum("C")
                .build();
    }

    private JsonNode getDescriptiveExpected() {
        return new JsonBuilder()
                .addField(JsonSchemaField.TITLE, title)
                .addField(JsonSchemaField.DESCRIPTION, desc)
                .addOneOf(new JsonBuilder()
                        .addField(JsonSchemaField.Disabled.CONST, "A")
                        .addField(JsonSchemaField.TITLE, "a")
                        .addField(JsonSchemaField.DESCRIPTION, "A_desc")
                        .build()
                )
                .addOneOf(new JsonBuilder()
                        .addField(JsonSchemaField.Disabled.CONST, "B")
                        .addField(JsonSchemaField.TITLE, "b")
                        .addField(JsonSchemaField.DESCRIPTION, "B_desc")
                        .build()
                )
                .addOneOf(new JsonBuilder()
                        .addField(JsonSchemaField.Disabled.CONST, "C")
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

        public SimpleEnum value;
    }

    class SimpleEnumRequiredClass {

        @JsonSchema(
                required = true
        )
        public SimpleEnum value;
    }

    class DescriptiveEnumClass {

        public DescriptiveEnum value;
    }

    class DoubleEnumClass {

        public DescriptiveEnum value1;
        public DescriptiveEnum value2;
    }

}
