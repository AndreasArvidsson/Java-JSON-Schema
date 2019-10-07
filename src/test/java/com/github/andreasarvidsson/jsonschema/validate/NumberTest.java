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

    @Test
    public void testIntegerMinFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMin = 1.9f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MINIMUM.toString(),
                2.0
        );
    }

    @Test
    public void testIntegerMaxFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMax = 2.1f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAXIMUM.toString(),
                2.0
        );
    }

    @Test
    public void testIntegerExMinFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueExMin = 2.0f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMin"),
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                2.0
        );
    }

    @Test
    public void testIntegerExMaxFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueExMax = 2.0f;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueExMax"),
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                2.0
        );
    }

    @Test
    public void testIntegerMultipleOfFailed() {
        final NumberClass instance = new NumberClass();
        instance.valueMultipleOf = 1.0f;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMultipleOf"),
                JsonSchemaField.MULTIPLE_OF.toString(),
                2.0
        );
    }

    class NumberClass {

        @JsonSchema(
                minimum = "2"
        )
        public float valueMin = 2;

        @JsonSchema(
                maximum = "2"
        )
        public float valueMax = 2;

        @JsonSchema(
                exclusiveMinimum = "2"
        )
        public float valueExMin = 3;

        @JsonSchema(
                exclusiveMaximum = "2"
        )
        public float valueExMax = 1;

        @JsonSchema(
                multipleOf = "2"
        )
        public float valueMultipleOf = 2;

    }

}
