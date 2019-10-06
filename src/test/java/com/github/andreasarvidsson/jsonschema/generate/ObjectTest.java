package com.github.andreasarvidsson.jsonschema.generate;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ObjectTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();
    private final String classTitle = "My class";
    private final String classDesc = "This is a class";
    private final String titleOuter = "My outer class";
    private final String descOuter = "This is an outer class";
    private final String fieldTitle = "My field";
    private final String fieldDesc = "This is a field";
    private final String dependency = "anoterValue";

    @Test
    public void testObj() {
        AssertJson.assertEquals(
                getExpected(),
                gen.generate(MyClass.class)
        );
    }

    @Test
    public void testOuterObj() {
        final ObjectNode valueOuter = getExpected();
        valueOuter.put(JsonSchemaField.TITLE.toString(), titleOuter);
        valueOuter.put(JsonSchemaField.DESCRIPTION.toString(), descOuter);
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addField(JsonSchemaField.TITLE, classTitle)
                .addField(JsonSchemaField.DESCRIPTION, classDesc)
                .addRequired("valueOuter")
                .addProperty("valueOuter", valueOuter)
                .build();

        AssertJson.assertEquals(
                expected,
                gen.generate(MyClass2.class)
        );
    }

    @Test
    public void testAnyGetterSetter() {
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(true)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.generate(AnyGetterSetterClass.class)
        );
    }

    @Test
    public void testDependencyClass() {
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false)
                .addDependencies("value", new String[]{dependency})
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build()
                )
                .addProperty(dependency, new JsonBuilder()
                        .setType(JsonType.STRING)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.generate(DependencyClass.class)
        );
    }

    private ObjectNode getExpected() {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .addField(JsonSchemaField.TITLE, classTitle)
                .addField(JsonSchemaField.DESCRIPTION, classDesc)
                .setAdditionalProps(false)
                .addProperty("value", new JsonBuilder()
                        .setType(JsonType.STRING)
                        .addField(JsonSchemaField.TITLE, fieldTitle)
                        .addField(JsonSchemaField.DESCRIPTION, fieldDesc)
                        .addField(JsonSchemaField.MIN_LENGTH, 1)
                        .build()
                )
                .build();
    }

    @JsonSchema(
            title = classTitle,
            description = classDesc
    )
    class MyClass {

        @JsonSchema(
                title = fieldTitle,
                description = fieldDesc,
                minLength = 1
        )
        public String value;

    }

    @JsonSchema(
            title = classTitle,
            description = classDesc
    )
    class MyClass2 {

        @JsonSchema(
                title = titleOuter,
                description = descOuter,
                required = true
        )
        public MyClass valueOuter;

    }

    class AnyGetterSetterClass {

        public String value;
        private Map<String, String> properties;

        @JsonAnySetter
        public void add(String key, String value) {
            properties.put(key, value);
        }

        @JsonAnyGetter
        public Map<String, String> get() {
            return properties;
        }

    }

    class DependencyClass {

        @JsonSchema(
                dependencies = {dependency}
        )
        public String value;

        public String anoterValue;

    }

}
