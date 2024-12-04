package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class SetTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final int minItems = 2;
    private final int maxItems = 5;
    private final String minimumStr = "2";
    private final int minimum = 2;

    @Test
    public void testMinItemsOk() {
        final MinItemsClass instance = new MinItemsClass();
        instance.values = get(minItems);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinItemsFailed() {
        final MinItemsClass instance = new MinItemsClass();
        instance.values = get(minItems - 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MIN_ITEMS.toString(),
                (long) minItems);
    }

    @Test
    public void testMaxItemsOk() {
        final MaxItemsClass instance = new MaxItemsClass();
        instance.values = get(maxItems);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxItemsFailed() {
        final MaxItemsClass instance = new MaxItemsClass();
        instance.values = get(maxItems + 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_ITEMS.toString(),
                (long) maxItems);
    }

    @Test
    public void testItemOk() {
        final Set<IntegerClass> instance = new HashSet<>(Arrays.asList(new IntegerClass(minimum)));
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testItemFail() {
        final Set<IntegerClass> instance = new HashSet<>(Arrays.asList(new IntegerClass(minimum - 1)));
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(PropertyPath.append(report.propertyPath, 0), "value"),
                JsonSchemaField.MINIMUM.toString(),
                (long) minimum);
    }

    private Set<Integer> get(final int size) {
        final Set<Integer> res = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            res.add(i);
        }
        return res;
    }

    class MinItemsClass {

        @JsonSchema(minItems = minItems)
        public Set<Integer> values;

    }

    class MaxItemsClass {

        @JsonSchema(maxItems = maxItems)
        public Set<Integer> values;

    }

    class IntegerClass {

        @JsonSchema(minimum = minimumStr)
        public final int value;

        public IntegerClass(final int value) {
            this.value = value;
        }

    }

}
