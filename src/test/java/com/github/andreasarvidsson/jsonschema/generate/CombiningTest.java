package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class CombiningTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testCombiningWithoutGroup() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value2", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value3", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addRequired("value1")
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addRequired("value2")
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addRequired("value3")
                                .build())
                        .build(),
                gen.generate(CombiningWithoutGroup.class));
    }

    @Test
    public void testCombiningWithGroup() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value2", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value3", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addRequired(Arrays.asList("value1", "value2"))
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addRequired("value3")
                                .build())
                        .build(),
                gen.generate(CombiningWithGroup.class));
    }

    @Test
    public void testCombiningWithGroupAndMultiple() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value2", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addProperty("value1", new JsonBuilder()
                                        .addField(JsonSchemaField.MIN_LENGTH, 2)
                                        .build())
                                .addProperty("value2", new JsonBuilder()
                                        .addField(JsonSchemaField.PATTERN, "\\s")
                                        .build())
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addProperty("value1", new JsonBuilder()
                                        .addField(JsonSchemaField.PATTERN, "\\d")
                                        .build())
                                .addProperty("value2", new JsonBuilder()
                                        .addField(JsonSchemaField.MIN_LENGTH, 5)
                                        .build())
                                .build())
                        .build(),
                gen.generate(CombiningWithGroupAndMultiple.class));
    }

    @Test
    public void testCombiningWithDependencies() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addProperty("value2", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addDependencies("value1", new String[] { "value2" })
                                .build())
                        .addOneOf(new JsonBuilder()
                                .addDependencies("value2", new String[] { "value1" })
                                .build())
                        .build(),
                gen.generate(CombiningWithDependencies.class));
    }

    @Test
    public void testCombiningSingleField() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.PATTERN, "\\d")
                                        .build())
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.MIN_LENGTH, 5)
                                        .build())
                                .build())
                        .build(),
                gen.generate(CombiningSingleField.class));
    }

    @Test
    public void testCombiningGroupZero() {
        AssertJson.assertEquals(
                new JsonBuilder()
                        .setType(JsonType.OBJECT)
                        .setAdditionalProps(false)
                        .addProperty("value1", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.PATTERN, "\\d")
                                        .build())
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.MIN_LENGTH, 5)
                                        .build())
                                .build())
                        .addProperty("value2", new JsonBuilder()
                                .setType(JsonType.STRING)
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.PATTERN, "\\s")
                                        .build())
                                .addOneOf(new JsonBuilder()
                                        .addField(JsonSchemaField.MIN_LENGTH, 2)
                                        .build())
                                .build())
                        .build(),
                gen.generate(CombiningGroupZero.class));
    }

    @Test
    public void testCombiningGroupZeroRequireException() {
        try {
            gen.generate(CombiningGroupZeroWithRequired.class);
            Assertions.fail("Expected runtime exception for required on combining group 0");
        } catch (final RuntimeException ex) {
        }
    }

    @Test
    public void testCombiningGroupZeroDependenciesException() {
        try {
            gen.generate(CombiningGroupZeroWithDependencies.class);
            Assertions.fail("Expected runtime exception for dependencies on combining group 0");
        } catch (final RuntimeException ex) {
        }
    }

    class CombiningWithoutGroup {

        @JsonSchema(combining = Combining.ONE_OF, required = true)
        public String value1;

        @JsonSchema(combining = Combining.ONE_OF, required = true)
        public String value2;

        @JsonSchema(combining = Combining.ONE_OF, required = true)
        public String value3;

    }

    class CombiningWithGroup {

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 1, required = true)
        public String value1;

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 1, required = true)
        public String value2;

        @JsonSchema(combining = Combining.ONE_OF, required = true)
        public String value3;

    }

    class CombiningWithGroupAndMultiple {

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 1, minLength = 2)
        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 2, pattern = "\\d")
        public String value1;

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 1, pattern = "\\s")
        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 2, minLength = 5)
        public String value2;
    }

    class CombiningWithDependencies {

        @JsonSchema(combining = Combining.ONE_OF, dependencies = { "value2" })
        public String value1;

        @JsonSchema(combining = Combining.ONE_OF, dependencies = { "value1" })
        public String value2;
    }

    class CombiningSingleField {

        @JsonSchema(combining = Combining.ONE_OF, pattern = "\\d")
        @JsonSchema(combining = Combining.ONE_OF, minLength = 5)
        public String value1;

    }

    class CombiningGroupZero {

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, pattern = "\\d")
        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, minLength = 5)
        public String value1;

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, pattern = "\\s")
        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, minLength = 2)
        public String value2;

    }

    class CombiningGroupZeroWithRequired {

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, required = true)
        public String value;

    }

    class CombiningGroupZeroWithDependencies {

        @JsonSchema(combining = Combining.ONE_OF, combiningGroup = 0, dependencies = { "value2" })
        public String value;

    }

}
