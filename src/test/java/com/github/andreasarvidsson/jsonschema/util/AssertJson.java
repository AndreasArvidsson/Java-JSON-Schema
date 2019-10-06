package com.github.andreasarvidsson.jsonschema.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Iterator;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class AssertJson {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();

    public static void assertEquals(final JsonNode expected, final JsonNode actual, final String message) {
        if (!equals(expected, actual)) {
            try {
                final String expectedStr = WRITER.writeValueAsString(expected);
                final String actualStr = WRITER.writeValueAsString(actual);
                Assertions.assertEquals(expectedStr, actualStr, message);
            }
            catch (JsonProcessingException ex) {
                Assertions.fail(ex);
            }
        }
    }

    public static void assertEquals(final JsonNode expected, final JsonNode actual) {
        if (!equals(expected, actual)) {
            try {
                final String expectedStr = WRITER.writeValueAsString(expected);
                final String actualStr = WRITER.writeValueAsString(actual);
                Assertions.assertEquals(expectedStr, actualStr);
            }
            catch (final JsonProcessingException ex) {
                Assertions.fail(ex);
            }
        }
    }

    public static void assertEqualsWithOrder(final JsonNode expected, final JsonNode actual) {
        try {
            final String expectedStr = WRITER.writeValueAsString(expected);
            final String actualStr = WRITER.writeValueAsString(actual);
            Assertions.assertEquals(expectedStr, actualStr);
        }
        catch (final JsonProcessingException ex) {
            Assertions.fail(ex);
        }
    }

    public static boolean equals(final JsonNode a, final JsonNode b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.getNodeType() != b.getNodeType()) {
            if (!a.asText().equals(b.asText())) {
                return false;
            }
        }
        if (a.size() != b.size()) {
            return false;
        }
        if (a.isArray()) {
            for (int i = 0; i < a.size(); ++i) {
                if (!equals(a.get(i), b.get(i))) {
                    return false;
                }
            }
        }
        if (a.isObject()) {
            final Iterator<String> it = a.fieldNames();
            while (it.hasNext()) {
                final String fieldName = it.next();
                if (!equals(a.get(fieldName), b.get(fieldName))) {
                    return false;
                }
            }
        }

        return a.asText().equals(b.asText());
    }

}
