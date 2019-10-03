package com.github.andreasarvidsson.jsonschema.generate;

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
    private final int min = 2;
    private final int max = 3;
    private final int minEx = 1;
    private final int maxEx = 4;
    private final int mult = 2;

    @Test
    public void testBytePrim() {
        AssertJson.assertEquals(
                getExcpected(Byte.MIN_VALUE, Byte.MAX_VALUE),
                gen.create(byte.class)
        );
    }

    @Test
    public void testByteClass() {
        AssertJson.assertEquals(
                getExcpected(Byte.MIN_VALUE, Byte.MAX_VALUE),
                gen.create(Byte.class)
        );
    }

    @Test
    public void testShortPrim() {
        AssertJson.assertEquals(
                getExcpected(Short.MIN_VALUE, Short.MAX_VALUE),
                gen.create(short.class)
        );
    }

    @Test
    public void testShortClass() {
        AssertJson.assertEquals(
                getExcpected(Short.MIN_VALUE, Short.MAX_VALUE),
                gen.create(Short.class)
        );
    }

    @Test
    public void testIntPrim() {
        AssertJson.assertEquals(
                getExcpected(Integer.MIN_VALUE, Integer.MAX_VALUE),
                gen.create(int.class)
        );
    }

    @Test
    public void testIntClass() {
        AssertJson.assertEquals(
                getExcpected(Integer.MIN_VALUE, Integer.MAX_VALUE),
                gen.create(Integer.class)
        );
    }

    @Test
    public void testLongPrim() {
        AssertJson.assertEquals(
                getExcpected(Long.MIN_VALUE, Long.MAX_VALUE),
                gen.create(long.class)
        );
    }

    @Test
    public void testLongClass() {
        AssertJson.assertEquals(
                getExcpected(Long.MIN_VALUE, Long.MAX_VALUE),
                gen.create(Long.class)
        );
    }

    @Test
    public void testBigInteger() {
        AssertJson.assertEquals(
                new JsonBuilder()
                .setType(JsonType.INTEGER)
                .build(),
                gen.create(BigInteger.class)
        );
    }

    @Test
    public void testNoAutoRange() {

        AssertJson.assertEquals(
                new JsonBuilder()
                .setType(JsonType.INTEGER)
                .build(),
                new JsonSchemaGenerator().hideSchemaField().disableAutoRangeNumbers()
                .create(int.class)
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
                .create(IntClass.class);
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
        Integer value;
    }

}
