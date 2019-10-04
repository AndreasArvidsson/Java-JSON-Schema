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
    public void testArrayBoolPrim() {
        final ArrayBoolPrim instance = new ArrayBoolPrim();
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    class ArrayBoolPrim {

        @JsonSchema(
                minimum = 5,
                required = true
        )
        Integer value;

        public ArrayBoolPrim(int value) {
            this.value = value;
        }

        public ArrayBoolPrim() {
            this.value = null;
        }

    }
    

}
