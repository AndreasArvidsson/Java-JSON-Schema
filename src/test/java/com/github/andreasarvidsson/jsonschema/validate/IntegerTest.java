package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class IntegerTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();

//    @Test
//    public void testIntegerMinFailed() {
//        final IntegerMinClass instance = new IntegerMinClass();
//        instance.value = 1;
//        final ValidationReport report = validator.validate(instance);
//        System.out.println(report.toString());
//        Assertions.assertFalse(report.isSuccess());
//        AssertError.assertError(
//                report.getErrors().get(0),
//                PropertyPath.append(report.propertyPath, "values"),
//                JsonSchemaField.MAX_ITEMS.toString(),
//                5L
//        );
//    }

    public static class IntegerMinClass {

        @JsonSchema(
                minimum = 2
        )
        public int value;

    }

    public static class IntegerMaxClass {

        @JsonSchema(
                maximum = 2
        )
        public int value;

    }

}
