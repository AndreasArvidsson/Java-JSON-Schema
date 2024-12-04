package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class JsonIgnoreTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final String minStr = "2";
    private final int min = 2;

    @Test
    public void testNormal() {
        final NormalClass instance = new NormalClass();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "value"),
                JsonSchemaField.MINIMUM.toString(),
                (long) min);
    }

    @Test
    public void testJsonIgnore() {
        final JsonIgnoreClass instance = new JsonIgnoreClass();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    class NormalClass {

        @JsonSchema(minimum = minStr)
        public int value = min - 1;

    }

    class JsonIgnoreClass {

        @JsonSchema(minimum = minStr)
        @JsonIgnore
        public int value = min - 1;

    }

}
