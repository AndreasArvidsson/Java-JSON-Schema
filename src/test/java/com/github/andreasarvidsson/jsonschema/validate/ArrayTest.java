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
    private final String minimumStr = "2";
    private final int minimum = 2;

    @Test
    public void testMinItemsOk() {
        final MinItems instance = new MinItems();
        instance.values = new int[minItems];
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinItemsFailed() {
        final MinItems instance = new MinItems();
        instance.values = new int[minItems - 1];
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MIN_ITEMS.toString(),
                (long) minItems);
    }

    @Test
    public void testMaxItemsOk() {
        final MaxItems instance = new MaxItems();
        instance.values = new int[maxItems];
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxItemsFailed() {
        final MaxItems instance = new MaxItems();
        instance.values = new int[maxItems + 1];
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_ITEMS.toString(),
                (long) maxItems);
    }

    @Test
    public void testArrayItemOk() {
        final IntegerClass[] instance = { new IntegerClass(minimum) };
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testArrayItemFail() {
        final IntegerClass[] instance = { new IntegerClass(minimum), new IntegerClass(minimum - 1) };
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(PropertyPath.append(report.propertyPath, 1), "value"),
                JsonSchemaField.MINIMUM.toString(),
                (long) minimum);
    }

    class MinItems {

        @JsonSchema(minItems = minItems)
        public int[] values;

    }

    class MaxItems {

        @JsonSchema(maxItems = maxItems)
        public int[] values;

    }

    class IntegerClass {

        @JsonSchema(minimum = minimumStr)
        public final int value;

        public IntegerClass(final int value) {
            this.value = value;
        }

    }

}
