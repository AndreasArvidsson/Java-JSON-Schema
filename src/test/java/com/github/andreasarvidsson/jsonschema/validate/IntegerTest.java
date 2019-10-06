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
        final IntegerMinClass instance = new IntegerMinClass();
        instance.value = 1;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report.getErrors().get(0),
                PropertyPath.append(report.propertyPath, "value"),
                JsonSchemaField.MINIMUM.toString(),
                2L
        );
    }

    @Test
    public void testIntegerMaxFailed() {
        final IntegerMaxClass instance = new IntegerMaxClass();
        instance.value = 3;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess(), report.toString());
        AssertError.assertError(
                report.getErrors().get(0),
                PropertyPath.append(report.propertyPath, "value"),
                JsonSchemaField.MAXIMUM.toString(),
                2L
        );
    }

    class IntegerMinClass {

        @JsonSchema(
                minimum = 2
        )
        public int value;

    }

    class IntegerMaxClass {

        @JsonSchema(
                maximum = 2
        )
        public int value;

    }

}
