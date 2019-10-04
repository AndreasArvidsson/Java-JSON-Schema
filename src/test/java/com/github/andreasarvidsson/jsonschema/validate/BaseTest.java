package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class BaseTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();

    @Test
    public void testRequiredOk() {
        final RequiredInt instance = new RequiredInt();
        instance.value = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testRequiredSubOk() {
        final RequiredSubInt instance = new RequiredSubInt();
        instance.wrapper = new RequiredInt();
        instance.wrapper.value = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testRequiredFail() {
        final RequiredInt instance = new RequiredInt();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess());
        assertError(
                report.getErrors().get(0),
                report.propertyPath,
                JsonSchemaField.Disabled.REQUIRED.toString(),
                "value"
        );
    }

    @Test
    public void testRequiredSubFail() {
        final RequiredSubInt instance = new RequiredSubInt();
        instance.wrapper = new RequiredInt();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess());
        assertError(
                report.getErrors().get(0),
                String.format("%s.%s", report.propertyPath, "wrapper"),
                JsonSchemaField.Disabled.REQUIRED.toString(),
                "value"
        );
    }

//              System.out.println(report.toString());
    private void assertError(final Error error, final String property, final String name, final String argument) {
        Assertions.assertEquals(property, error.property, "Error.property");
        Assertions.assertEquals(name, error.name, "Error.name");
        Assertions.assertEquals(argument, error.argument, "Error.argument");
    }

    public static class RequiredInt {

        @JsonSchema(
                required = true
        )
        public Integer value;

    }

    public static class RequiredSubInt {

        @JsonSchema(
                required = true
        )
        public RequiredInt wrapper;

    }

}
