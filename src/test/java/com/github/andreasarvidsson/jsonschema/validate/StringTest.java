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

    @Test
    public void testStringMinFailed() {
        final StringClass instance = new StringClass();
        instance.valueMin = "h";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "valueMin"),
                JsonSchemaField.MIN_LENGTH.toString(),
                5L
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
                5L
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
                "\\d+"
        );
    }

    class StringClass {

        @JsonSchema(
                minLength = 5
        )
        public String valueMin = "hello";

        @JsonSchema(
                maxLength = 5
        )
        public String valueMax = "hello";

        @JsonSchema(
                pattern = "\\d+"
        )
        public String valuePattern = "3";

    }

}
