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

    @Test
    public void testIntegerMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMin = 1;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MINIMUM.toString(),
                2L
        );
    }

    @Test
    public void testIntegerMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMax = 3;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAXIMUM.toString(),
                2L
        );
    }

    @Test
    public void testIntegerExMinFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMin = 2;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMin"),
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                2L
        );
    }

    @Test
    public void testIntegerExMaxFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueExMax = 2;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMax"),
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                2L
        );
    }

    @Test
    public void testIntegerMultipleOfFailed() {
        final IntegerClass instance = new IntegerClass();
        instance.valueMultipleOf = 1;
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
                minimum = "2"
        )
        public int valueMin = 2;

        @JsonSchema(
                maximum = "2"
        )
        public int valueMax = 2;

        @JsonSchema(
                exclusiveMinimum = "2"
        )
        public int valueExMin = 3;

        @JsonSchema(
                exclusiveMaximum = "2"
        )
        public int valueExMax = 1;

        @JsonSchema(
                multipleOf = "2"
        )
        public int valueMultipleOf = 2;

    }

}
