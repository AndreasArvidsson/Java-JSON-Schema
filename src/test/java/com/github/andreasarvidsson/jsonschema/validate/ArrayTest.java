package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ArrayTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final long minItems = 2;
    private final long maxItems = 5;

    @Test
    public void testMinItemsFailed() {
        final ArrayMinItems instance = new ArrayMinItems();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MIN_ITEMS.toString(),
                minItems
        );
    }

    @Test
    public void testMaxItemsFailed() {
        final ArrayMaxItems instance = new ArrayMaxItems();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_ITEMS.toString(),
                maxItems
        );
    }

    class ArrayMinItems {

        @JsonSchema(
                minItems = minItems
        )
        public int[] values = new int[1];

    }

    class ArrayMaxItems {

        @JsonSchema(
                maxItems = maxItems
        )
        public int[] values = new int[7];

    }

}
