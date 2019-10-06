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
public class ClassTest {

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
        AssertError.assertError(
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
        AssertError.assertError(
                report.getErrors().get(0),
                PropertyPath.append(report.propertyPath, "wrapper"),
                JsonSchemaField.Disabled.REQUIRED.toString(),
                "value"
        );
    }

    class RequiredInt {

        @JsonSchema(
                required = true
        )
        public Integer value;

    }

    class RequiredSubInt {

        @JsonSchema(
                required = true
        )
        public RequiredInt wrapper;

    }

}
