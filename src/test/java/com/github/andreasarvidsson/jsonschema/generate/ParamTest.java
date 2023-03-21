package com.github.andreasarvidsson.jsonschema.generate;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParamTest {

    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testParam() {
        final ObjectNode properties = new JsonBuilder()
            .addField("value1", getObject("value", getString()))
            .addField("value2", getObject("value", getInt()))
            .addField("value3", getArray(getObject("value", getNumber())))
            .build();
        AssertJson.assertEquals(
            getObject(properties),
            gen.generate(ParamClass.class)
        );
    }

    @Test
    public void testSub1() {
        final ObjectNode properties = new JsonBuilder()
            .addField("value", getString())
            .addField("values", getArray(getString()))
            .build();
        AssertJson.assertEquals(
            getObject(properties),
            gen.generate(Sub.class)
        );
    }

    private ObjectNode getObject(final String key, final JsonNode value) {
        return new JsonBuilder()
            .setType(JsonType.OBJECT)
            .setAdditionalProps(false)
            .addProperty(key, value)
            .build();
    }

    private ObjectNode getObject(final ObjectNode properties) {
        return new JsonBuilder()
            .setType(JsonType.OBJECT)
            .setAdditionalProps(false)
            .setProperties(properties)
            .build();
    }

    private ObjectNode getArray(final ObjectNode items) {
        return new JsonBuilder()
            .setType(JsonType.ARRAY)
            .setItems(items)
            .build();
    }

    private ObjectNode getString() {
        return new JsonBuilder().setType(JsonType.STRING).build();
    }

    private ObjectNode getNumber() {
        return new JsonBuilder().setType(JsonType.NUMBER).build();
    }

    private ObjectNode getInt() {
        return new JsonBuilder()
            .setType(JsonType.INTEGER)
            .addField("minimum", Integer.MIN_VALUE)
            .addField("maximum", Integer.MAX_VALUE)
            .build();
    }

    class MyDataClass<T> {

        public T value;
    }

    class ParamClass {

        public MyDataClass<String> value1;
        public MyDataClass<Integer> value2;
        public List<MyDataClass<Double>> value3;

    }

    class Parent<T> {
        public T value;
        public List<T> values;
    }

    class Sub extends Parent<String> {

    }

}
