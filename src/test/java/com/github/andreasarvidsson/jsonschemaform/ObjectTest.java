package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
                gen.create(MyClass.class)
        );
    }

    @Test
    public void testOuterObj() {
        final ObjectNode valueOuter = getExpected();
        valueOuter.put(JsonSchemaField.TITLE.toString(), titleOuter);
        valueOuter.put(JsonSchemaField.DESCRIPTION.toString(), descOuter);
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .addField(JsonSchemaField.TITLE, classTitle)
                .addField(JsonSchemaField.DESCRIPTION, classDesc)
                .addField("additionalProperties", false)
                .addRequired("valueOuter")
                .addProperty("valueOuter", valueOuter)
                .build();

        AssertJson.assertEquals(
                expected,
                gen.create(MyClass2.class)
        );
    }

    @Test
    public void testAnyGetterSetter() {
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .addField("additionalProperties", true)
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.STRING)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.create(AnyGetterSetterClass.class)
        );
    }

    @Test
    public void testDependencyClass() {
        final ObjectNode expected = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .addField("additionalProperties", false)
                .addDependencies("value", new String[]{dependency})
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.STRING)
                        .build()
                )
                .addProperty(dependency, new JsonBuilder()
                        .setTypeNull(JsonType.STRING)
                        .build()
                )
                .build();
        AssertJson.assertEquals(
                expected,
                gen.create(DependencyClass.class)
        );
    }

    private ObjectNode getExpected() {
        return new JsonBuilder()
                .setType(JsonType.OBJECT)
                .addField(JsonSchemaField.TITLE, classTitle)
                .addField(JsonSchemaField.DESCRIPTION, classDesc)
                .addField("additionalProperties", false)
                .addProperty("value", new JsonBuilder()
                        .setTypeNull(JsonType.STRING)
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
        String value;

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
        MyClass valueOuter;

    }

    class AnyGetterSetterClass {

        String value;
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
        String value;

        String anoterValue;

    }

}
