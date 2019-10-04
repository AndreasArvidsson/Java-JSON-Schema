package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class ArrayTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();

    @Test
    public void testMinItemsFailed() {
        final ArrayMinItems instance = new ArrayMinItems();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess());
        AssertError.assertError(
                report.getErrors().get(0),
                String.format("%s.%s", report.propertyPath, "values"),
                JsonSchemaField.MIN_ITEMS.toString(),
                2L
        );
    }

    @Test
    public void testMaxItemsFailed() {
        final ArrayMaxItems instance = new ArrayMaxItems();
        final ValidationReport report = validator.validate(instance);
//        System.out.println(report.toString());
        Assertions.assertFalse(report.isSuccess());
        AssertError.assertError(
                report.getErrors().get(0),
                String.format("%s.%s", report.propertyPath, "values"),
                JsonSchemaField.MAX_ITEMS.toString(),
                5L
        );
    }

    public static class ArrayMinItems {

        @JsonSchema(
                minItems = 2
        )
        public int[] values = new int[1];

    }

    public static class ArrayMaxItems {

        @JsonSchema(
                maxItems = 5
        )
        public int[] values = new int[7];

    }

}
