package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonValueTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();

    @Test
    public void testNormalClass() {
        final NormalClass instance = new NormalClass();
        instance.value = "str";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testJsonValue() {
        final JsonValueClass instance = new JsonValueClass();
        instance.value = "str";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testJsonValueWithPatternOk() {
        final PatternClass instance = new PatternClass();
        instance.value = "5";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testJsonValueWithPatternFail() {
        final PatternClass instance = new PatternClass();
        instance.value = "a";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                report.propertyPath,
                JsonSchemaField.PATTERN.toString(),
                PatternClass.PATTERN
        );
    }

    class NormalClass {

        public String value;

    }

    public static class JsonValueClass {

        public String value;

        @JsonValue
        public String getValue() {
            return value;
        }

    }

    @JsonSchema(
            pattern = PatternClass.PATTERN
    )
    public static class PatternClass {

        private final static String PATTERN = "\\d+";

        public String value;

        @JsonValue
        public String getValue() {
            return value;
        }

    }

}
