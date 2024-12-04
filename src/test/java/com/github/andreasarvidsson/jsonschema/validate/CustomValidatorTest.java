package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import com.github.andreasarvidsson.jsonschema.validate.validators.Validator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class CustomValidatorTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator()
            .addCustomValidator(MyClass.class, new MyValidator());

    @Test
    public void testCustomOk() {
        final MyClass instance = new MyClass();
        instance.value = 1;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testCustomFail() {
        final MyClass instance = new MyClass();
        instance.value = 0;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                report.propertyPath,
                JsonSchemaField.MINIMUM.toString(),
                1);
    }

    class MyValidator implements Validator {

        @Override
        public void validateClass(final List<Error> errors, final String path, final Object instance) {
            final int value = ((MyClass) instance).value;
            if (value < 1) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MINIMUM.toString(),
                        1,
                        String.format("Does not meet minimum of %d", 1),
                        null,
                        instance));
            }
        }

        @Override
        public void validateSchema(final List<Error> errors, final String path, final Object instance,
                final JsonSchema jsonSchema) {
        }

    }

    class MyClass {

        public int value;

    }

}
