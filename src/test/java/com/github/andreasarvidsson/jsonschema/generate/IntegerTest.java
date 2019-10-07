package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class IntegerTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String title = "title";
    private final String desc = "desc";
    private final String min = "2";
    private final String max = "3";
    private final String minEx = "1";
    private final String maxEx = "4";
    private final String mult = "2";

    @Test
    public void testBytePrim() {
        AssertJson.assertEquals(
                getExcpected(Byte.MIN_VALUE, Byte.MAX_VALUE),
                gen.generate(byte.class)
        );
    }

    @Test
    public void testByteClass() {
        AssertJson.assertEquals(
                getExcpected(Byte.MIN_VALUE, Byte.MAX_VALUE),
                gen.generate(Byte.class)
        );
    }

    @Test
    public void testShortPrim() {
        AssertJson.assertEquals(
                getExcpected(Short.MIN_VALUE, Short.MAX_VALUE),
                gen.generate(short.class)
        );
    }

    @Test
    public void testShortClass() {
        AssertJson.assertEquals(
                getExcpected(Short.MIN_VALUE, Short.MAX_VALUE),
                gen.generate(Short.class)
        );
    }

    @Test
    public void testIntPrim() {
        AssertJson.assertEquals(
                getExcpected(Integer.MIN_VALUE, Integer.MAX_VALUE),
                gen.generate(int.class)
        );
    }

    @Test
    public void testIntClass() {
        AssertJson.assertEquals(
                getExcpected(Integer.MIN_VALUE, Integer.MAX_VALUE),
                gen.generate(Integer.class)
        );
    }

    @Test
    public void testLongPrim() {
        AssertJson.assertEquals(
                getExcpected(Long.MIN_VALUE, Long.MAX_VALUE),
                gen.generate(long.class)
        );
    }

    @Test
    public void testLongClass() {
        AssertJson.assertEquals(
                getExcpected(Long.MIN_VALUE, Long.MAX_VALUE),
                gen.generate(Long.class)
        );
    }

    @Test
    public void testBigInteger() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.INTEGER)
                        .build(),
                gen.generate(BigInteger.class)
        );
    }

    @Test
    public void testNoAutoRange() {

        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.INTEGER)
                        .build(),
                new JsonSchemaGenerator().hideSchemaField().disableAutoRangeNumbers()
                        .generate(int.class)
        );
    }

    public void testIntAnotations() {
        final JsonNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value",
                        new JsonBuilder()
                                .setType(JsonType.INTEGER)
                                .addField(JsonSchemaField.TITLE, title)
                                .addField(JsonSchemaField.DESCRIPTION, desc)
                                .addField(JsonSchemaField.MINIMUM, min)
                                .addField(JsonSchemaField.MAXIMUM, max)
                                .addField(JsonSchemaField.EXCLUSIVE_MINIMUM, minEx)
                                .addField(JsonSchemaField.EXCLUSIVE_MAXIMUM, maxEx)
                                .addField(JsonSchemaField.MULTIPLE_OF, mult)
                                .build()
                )
                .build();
        final JsonNode actual = new JsonSchemaGenerator()
                .hideSchemaField()
                .disableAutoRangeNumbers()
                .generate(IntClass.class);
        AssertJson.assertEquals(expected, actual);
    }

    private JsonNode getExcpected(final long min, final long max) {
        return new JsonBuilder()
                .setType(JsonType.INTEGER)
                .addField(JsonSchemaField.MINIMUM, min)
                .addField(JsonSchemaField.MAXIMUM, max)
                .build();
    }

    class IntClass {

        @JsonSchema(
                title = title,
                description = desc,
                minimum = min,
                maximum = max,
                exclusiveMinimum = minEx,
                exclusiveMaximum = maxEx,
                multipleOf = mult
        )
        public Integer value;
    }

}
