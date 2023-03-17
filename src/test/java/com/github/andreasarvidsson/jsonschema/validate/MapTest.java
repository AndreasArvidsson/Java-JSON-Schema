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
    private final String minimumStr = "2";
    private final int minimum = 2;

    @Test
    public void testMinPropertiesOk() {
        final MinPropertiesClass instance = new MinPropertiesClass();
        add(instance.values, minProperties);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinPropertiesFailed() {
        final MinPropertiesClass instance = new MinPropertiesClass();
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
        final MaxPropertiesClass instance = new MaxPropertiesClass();
        add(instance.values, maxProperties);
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMaxPropertiesFailed() {
        final MaxPropertiesClass instance = new MaxPropertiesClass();
        add(instance.values, maxProperties + 1);
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(report.propertyPath, "values"),
                JsonSchemaField.MAX_PROPERTIES.toString(),
                (long) maxProperties
        );
    }

    @Test
    public void testItemOk() {
        final Map<String, IntegerClass> instance = new HashMap<>();
        instance.put("key", new IntegerClass(minimum));
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testItemFail() {
        final Map<String, IntegerClass> instance = new HashMap<>();
        instance.put("key", new IntegerClass(minimum - 1));
        final ValidationReport report = validator.validate(instance);
        AssertError.assertError(
                report,
                PropertyPath.append(PropertyPath.append(report.propertyPath, "key"), "value"),
                JsonSchemaField.MINIMUM.toString(),
                (long) minimum
        );
    }

    private void add(final Map<String, Integer> map, final int size) {
        for (int i = 0; i < size; ++i) {
            map.put(String.valueOf((char) ('A' + i)), i);
        }
    }

    class MinPropertiesClass {

        @JsonSchema(
                minProperties = minProperties
        )
        public Map<String, Integer> values = new HashMap<>();

    }

    class MaxPropertiesClass {

        @JsonSchema(
                maxProperties = maxProperties
        )
        public Map<String, Integer> values = new HashMap<>();

    }

    class IntegerClass {

        @JsonSchema(
                minimum = minimumStr
        )
        public final int value;

        public IntegerClass(final int value) {
            this.value = value;
        }

    }

}
