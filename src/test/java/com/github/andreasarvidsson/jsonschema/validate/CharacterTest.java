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
public class CharacterTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String constant = "A";

    @Test
    public void testConstOk() {
        final CharClass instance = new CharClass();
        instance.value = 'A';
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testConstFail() {
        final CharClass instance = new CharClass();
        instance.value = 'B';
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "value"),
                JsonSchemaField.CONST.toString(),
                constant.charAt(0)
        );
    }

    class CharClass {

        @JsonSchema(
                constant = constant
        )
        public char value;

    }

}
