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
    public void testIntegerMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMin = value - 1;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MINIMUM.toString(),
                (long) value
        );
    }

    @Test
    public void testIntegerMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMax = value + 1;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAXIMUM.toString(),
                (long) value
        );
    }

    @Test
    public void testIntegerExMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMin = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMin"),
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                (long) value
        );
    }

    @Test
    public void testIntegerExMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMax = value;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMax"),
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                (long) value
        );
    }

    @Test
    public void testIntegerMultipleOfFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMultipleOf = value + 1;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMultipleOf"),
                JsonSchemaField.MULTIPLE_OF.toString(),
                2L
        );
    }

    class IntegerClass {

        @JsonSchema(
                minimum = valueStr
        )
        public int valueMin = value;

        @JsonSchema(
                maximum = valueStr
        )
        public int valueMax = value;

        @JsonSchema(
                exclusiveMinimum = valueStr
        )
        public int valueExMin = value + 1;

        @JsonSchema(
                exclusiveMaximum = valueStr
        )
        public int valueExMax = value - 1;

        @JsonSchema(
                multipleOf = valueStr
        )
        public int valueMultipleOf = value;

    }

}
