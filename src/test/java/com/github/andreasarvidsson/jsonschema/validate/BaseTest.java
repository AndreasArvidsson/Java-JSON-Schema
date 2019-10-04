package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
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
    public void testRequiredFail() {
        final RequiredInt instance = new RequiredInt();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertFalse(report.isSuccess());
        System.out.println(report.toString());
    }

    public static class RequiredInt {

        @JsonSchema(
                required = true
        )
        public Integer value;

    }

}
