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
public class BoolTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String constant = "true";

    @Test
    public void testConstOk() {
        final BoolClass instance = new BoolClass();
        instance.value = true;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testConstFail() {
        final BoolClass instance = new BoolClass();
        instance.value = false;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "value"),
                JsonSchemaField.CONST.toString(),
                Boolean.parseBoolean(constant)
        );
    }

    class BoolClass {

        @JsonSchema(
                constant = constant
        )
        public Boolean value;

    }

}
