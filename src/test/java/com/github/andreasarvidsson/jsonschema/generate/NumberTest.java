package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
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
    private final String min = "2";
    private final String max = "3";
    private final String minEx = "1";
    private final String maxEx = "4";
    private final String mult = "2";

    @Test
    public void testFloatPrim() {
        AssertJson.assertEquals(expected, gen.generate(float.class));
    }

    @Test
    public void testFloatClass() {
        AssertJson.assertEquals(expected, gen.generate(Float.class));
    }

    @Test
    public void testDoublePrim() {
        AssertJson.assertEquals(expected, gen.generate(double.class));
    }

    @Test
    public void testDoubleClass() {
        AssertJson.assertEquals(expected, gen.generate(Double.class));
    }

    @Test
    public void testBigDecimal() {
        AssertJson.assertEquals(expected, gen.generate(BigDecimal.class));
    }

    public void testNumberAnotations() {
        final JsonNode exp = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.NUMBER)
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
                .generate(NumberClass.class);
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
        public Double value;
    }

}
