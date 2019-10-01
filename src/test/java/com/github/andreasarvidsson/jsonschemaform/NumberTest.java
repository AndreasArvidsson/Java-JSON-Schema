package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class NumberTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final JsonNode expected = new JsonBuilder().setType(JsonType.NUMBER).build();
    private final String title = "title";
    private final String desc = "desc";
    private final int min = 2;
    private final int max = 3;
    private final int minEx = 1;
    private final int maxEx = 4;
    private final int mult = 2;

    @Test
    public void testFloatPrim() {
        AssertJson.assertEquals(expected, gen.create(float.class));
    }

    @Test
    public void testFloatClass() {
        AssertJson.assertEquals(expected, gen.create(Float.class));
    }

    @Test
    public void testDoublePrim() {
        AssertJson.assertEquals(expected, gen.create(double.class));
    }

    @Test
    public void testDoubleClass() {
        AssertJson.assertEquals(expected, gen.create(Double.class));
    }

    @Test
    public void testBigDecimal() {
        AssertJson.assertEquals(expected, gen.create(BigDecimal.class));
    }

    public void testNumberAnotations() {
        final JsonNode exp = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value",
                        new JsonBuilder()
                        .setTypeNull(JsonType.NUMBER)
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
                .create(NumberClass.class);
        AssertJson.assertEquals(exp, actual);
    }

    class NumberClass {

        @JsonSchema(
                title = title,
                description = desc,
                minimum = min,
                maximum = max,
                exclusiveMinimum = minEx,
                exclusiveMaximum = maxEx,
                multipleOf = mult
        )
        Double value;
    }

}
