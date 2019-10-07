package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class StringTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String pattern = "\\d+";
    private final long minMaxLength = 5;

    @Test
    public void testStringMinFailed() {
        final StringClass instance = new StringClass();
        instance.valueMin = "h";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MIN_LENGTH.toString(),
                minMaxLength
        );
    }

    @Test
    public void testStringMaxFailed() {
        final StringClass instance = new StringClass();
        instance.valueMax = "hello world";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMax"),
                JsonSchemaField.MAX_LENGTH.toString(),
                minMaxLength
        );
    }

    @Test
    public void testStringPatternFailed() {
        final StringClass instance = new StringClass();
        instance.valuePattern = "a5";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valuePattern"),
                JsonSchemaField.PATTERN.toString(),
                pattern
        );
    }

    class StringClass {

        @JsonSchema(
                minLength = minMaxLength
        )
        public String valueMin = "hello";

        @JsonSchema(
                maxLength = minMaxLength
        )
        public String valueMax = "hello";

        @JsonSchema(
                pattern = pattern
        )
        public String valuePattern = "3";

    }

}
