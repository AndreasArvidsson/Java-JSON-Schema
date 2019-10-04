package com.github.andreasarvidsson.jsonschema.util;

import org.junit.jupiter.api.Assertions;
import com.github.andreasarvidsson.jsonschema.validate.Error;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class AssertError {

    public static void assertError(final Error error, final String property, final String name, final Object argument) {
        Assertions.assertEquals(property, error.property, "Error.property");
        Assertions.assertEquals(name, error.name, "Error.name");
        Assertions.assertEquals(argument, error.argument, "Error.argument");
    }

}
