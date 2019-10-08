package com.github.andreasarvidsson.jsonschema.util;

import com.github.andreasarvidsson.jsonschema.validate.Error;
import com.github.andreasarvidsson.jsonschema.validate.ValidationReport;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class AssertError {

    public static void assertError(final ValidationReport report, final String property, final String name, final Object argument) {
        Assertions.assertFalse(report.isSuccess(), report.toString());
        Assertions.assertEquals(1, report.getErrors().size(), report.toString());
        assertError(report.getErrors().get(0), property, name, argument);
    }

    public static void assertErrorMessage(final ValidationReport report, final String property, final String name, final String message) {
        Assertions.assertFalse(report.isSuccess(), report.toString());
        Assertions.assertEquals(1, report.getErrors().size(), report.toString());
        assertErroMessage(report.getErrors().get(0), property, name, message);
    }

    public static void assertError(final Error error, final String property, final String name, final Object argument) {
        Assertions.assertEquals(property, error.property, "Error.property");
        Assertions.assertEquals(name, error.name, "Error.name");
        Assertions.assertEquals(argument, error.argument, "Error.argument");
    }

    public static void assertErroMessage(final Error error, final String property, final String name, final String message) {
        Assertions.assertEquals(property, error.property, "Error.property");
        Assertions.assertEquals(name, error.name, "Error.name");
        Assertions.assertEquals(message, error.message, "Error.message");
    }

}
