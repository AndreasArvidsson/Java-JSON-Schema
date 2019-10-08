package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class MapTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();
    private final int minProperties = 2;
    private final int maxProperties = 1;

    @Test
    public void testMinPropertiesOk() {
        final MapMinProperties instance = new MapMinProperties();
        add(instance.values, minProperties);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinPropertiesFailed() {
        final MapMinProperties instance = new MapMinProperties();
        add(instance.values, minProperties - 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MIN_PROPERTIES.toString(),
                (long) minProperties
        );
    }

    @Test
    public void testMaxPropertiesOk() {
        final MapMaxProperties instance = new MapMaxProperties();
        add(instance.values, maxProperties);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxPropertiesFailed() {
        final MapMaxProperties instance = new MapMaxProperties();
        add(instance.values, maxProperties + 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_PROPERTIES.toString(),
                (long) maxProperties
        );
    }

    private void add(final Map map, final int size) {
        for (int i = 0; i < size; ++i) {
            map.put(String.valueOf((char) ('A' + i)), i);
        }
    }

    class MapMinProperties {

        @JsonSchema(
                minProperties = minProperties
        )
        public Map values = new HashMap();

    }

    class MapMaxProperties {

        @JsonSchema(
                maxProperties = maxProperties
        )
        public Map values = new HashMap();

    }
}
