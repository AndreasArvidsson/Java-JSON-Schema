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
public class NumberTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String valueStr = "2";
    private final float value = 2;

    @Test
    public void testMinFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMin = value - 0.1f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MINIMUM.toString(),
                (double) value);
    }

    @Test
    public void testMaxFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMax = value + 0.1f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAXIMUM.toString(),
                (double) value);
    }

    @Test
    public void testExMinFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueExMin = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMin"),
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                (double) value);
    }

    @Test
    public void testExMaxFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueExMax = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMax"),
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                (double) value);
    }

    @Test
    public void testMultipleOfFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMultipleOf = value + 0.1f;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMultipleOf"),
                JsonSchemaField.MULTIPLE_OF.toString(),
                (double) value);
    }

    @Test
    public void testConstFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueConstant = value + 0.1f;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueConstant"),
                JsonSchemaField.CONST.toString(),
                (double) value);
    }

    class NumberClass {

        @JsonSchema(minimum = valueStr)
        public float valueMin = value;

        @JsonSchema(maximum = valueStr)
        public float valueMax = value;

        @JsonSchema(exclusiveMinimum = valueStr)
        public float valueExMin = value + 1;

        @JsonSchema(exclusiveMaximum = valueStr)
        public float valueExMax = value - 1;

        @JsonSchema(multipleOf = valueStr)
        public float valueMultipleOf = value;

        @JsonSchema(constant = valueStr)
        public float valueConstant = value;

    }

}
