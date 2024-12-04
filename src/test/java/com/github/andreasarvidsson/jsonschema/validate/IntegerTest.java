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
public class IntegerTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String valueStr = "2";
    private final int value = 2;

    @Test
    public void testMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMin = value - 1;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MINIMUM.toString(),
                (long) value);
    }

    @Test
    public void testMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMax = value + 1;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAXIMUM.toString(),
                (long) value);
    }

    @Test
    public void testExMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMin = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMin"),
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                (long) value);
    }

    @Test
    public void testExMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMax = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMax"),
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                (long) value);
    }

    @Test
    public void testMultipleOfFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMultipleOf = value + 1;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMultipleOf"),
                JsonSchemaField.MULTIPLE_OF.toString(),
                (long) value);
    }

    @Test
    public void testConstFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueConstant = value + 1;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueConstant"),
                JsonSchemaField.CONST.toString(),
                (long) value);
    }

    class IntegerClass {

        @JsonSchema(minimum = valueStr)
        public int valueMin = value;

        @JsonSchema(maximum = valueStr)
        public int valueMax = value;

        @JsonSchema(exclusiveMinimum = valueStr)
        public int valueExMin = value + 1;

        @JsonSchema(exclusiveMaximum = valueStr)
        public int valueExMax = value - 1;

        @JsonSchema(multipleOf = valueStr)
        public int valueMultipleOf = value;

        @JsonSchema(constant = valueStr)
        public int valueConstant = value;

    }

}
