package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ArrayTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final int minItems = 2;
    private final int maxItems = 5;

    @Test
    public void testMinItemsOk() {
        final ArrayMinItems instance = new ArrayMinItems();
        instance.values = new int[minItems];
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinItemsFailed() {
        final ArrayMinItems instance = new ArrayMinItems();
        instance.values = new int[minItems - 1];
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MIN_ITEMS.toString(),
                (long)minItems
        );
    }

    @Test
    public void testMaxItemsOk() {
        final ArrayMaxItems instance = new ArrayMaxItems();
        instance.values = new int[maxItems];
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxItemsFailed() {
        final ArrayMaxItems instance = new ArrayMaxItems();
        instance.values = new int[maxItems + 1];
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_ITEMS.toString(),
                (long)maxItems
        );
    }

    class ArrayMinItems {

        @JsonSchema(
                minItems = minItems
        )
        public int[] values;

    }

    class ArrayMaxItems {

        @JsonSchema(
                maxItems = maxItems
        )
        public int[] values;

    }

}
