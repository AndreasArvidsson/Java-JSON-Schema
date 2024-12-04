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
public class StringTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String pattern = "\\d+";
    private final String constant = "Hello";
    private final long length = 5;

    @Test
    public void testMinLengthOk() {
        final StringClass instance = new StringClass();
        instance.valueMin = get(length);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinLengthFailed() {
        final StringClass instance = new StringClass();
        instance.valueMin = get(length - 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MIN_LENGTH.toString(),
                length);
    }

    @Test
    public void testMaxLengthOk() {
        final StringClass instance = new StringClass();
        instance.valueMax = get(length);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxLengthFailed() {
        final StringClass instance = new StringClass();
        instance.valueMax = get(length + 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAX_LENGTH.toString(),
                length);
    }

    @Test
    public void testPatternOk() {
        final StringClass instance = new StringClass();
        instance.valuePattern = "5";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testPatternFailed() {
        final StringClass instance = new StringClass();
        instance.valuePattern = "5a";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valuePattern"),
                JsonSchemaField.PATTERN.toString(),
                pattern);
    }

    @Test
    public void testConstOk() {
        final StringClass instance = new StringClass();
        instance.valueConstant = constant;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testConstFailed() {
        final StringClass instance = new StringClass();
        instance.valueConstant = "a";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueConstant"),
                JsonSchemaField.CONST.toString(),
                constant);
    }

    private String get(final long size) {
        final StringBuilder sb = new StringBuilder();
        for (long i = 0; i < size; ++i) {
            sb.append((char) ('A' + i));
        }
        return sb.toString();
    }

    class StringClass {

        @JsonSchema(minLength = length)
        public String valueMin;

        @JsonSchema(maxLength = length)
        public String valueMax;

        @JsonSchema(pattern = pattern)
        public String valuePattern;

        @JsonSchema(constant = constant)
        public String valueConstant;

    }

}
