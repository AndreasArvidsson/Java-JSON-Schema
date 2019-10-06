package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andreasarvidsson.jsonschema.util.AssertJson;
import com.github.andreasarvidsson.jsonschema.util.JsonBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class JsonPropertyOrderTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchemaGenerator gen = new JsonSchemaGenerator().hideSchemaField();

    @Test
    public void testNormalExtendingNormal() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new NormalExtendingNormal()),
                gen.create(NormalExtendingNormal.class)
        );
    }

    @Test
    public void testNormalExtendingOrdered() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new NormalExtendingOrdered()),
                gen.create(NormalExtendingOrdered.class)
        );
    }

    @Test
    public void testOrderedExtendingNormal() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new OrderedExtendingNormal()),
                gen.create(OrderedExtendingNormal.class)
        );
    }

    @Test
    public void testOrderedExtendingOrdered() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new OrderedExtendingOrdered()),
                gen.create(OrderedExtendingOrdered.class)
        );
    }

    @Test
    public void testAlphabetically() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new Alphabetically()),
                gen.create(Alphabetically.class)
        );
    }

    @Test
    public void testAlphabeticallyAndValue() {
        AssertJson.assertEqualsWithOrder(
                getExpected(new AlphabeticallyAndValue()),
                gen.create(AlphabeticallyAndValue.class)
        );
    }

    class NormalBaseClass {

        public String valueB1;
        public String valueB2;
    }

    @JsonPropertyOrder({"valueB2", "valueB1"})
    class OrderedBaseClass {

        public String valueB1;
        public String valueB2;
    }

    class NormalExtendingNormal extends NormalBaseClass {

        public String value1;
        public String value2;
    }

    class NormalExtendingOrdered extends OrderedBaseClass {

        public String value1;
        public String value2;
    }

    class OrderedExtendingNormal extends NormalBaseClass {

        public String value1;
        public String value2;
    }

    @JsonPropertyOrder({"value2", "value1"})
    class OrderedExtendingOrdered extends OrderedBaseClass {

        public String value1;
        public String value2;
    }

    @JsonPropertyOrder(alphabetic = true)
    class Alphabetically {

        public String g;
        public String a;
        public String b;
        public String c22;
        public String c;
    }

    @JsonPropertyOrder(alphabetic = true, value = {"b", "c22"})
    class AlphabeticallyAndValue {

        public String g;
        public String a;
        public String b;
        public String c22;
        public String c;
    }

    private JsonNode getExpected(final Object instance) {
        final JsonBuilder b = new JsonBuilder()
                .setType(JsonType.OBJECT)
                .setAdditionalProps(false);
        for (final String property : getPropertyOrder(instance)) {
            b.addProperty(property,
                    new JsonBuilder().setType(JsonType.STRING).build()
            );
        }
        return b.build();
    }

    private List<String> getPropertyOrder(final Object instance) {
        final List<String> result = new ArrayList();
        final JsonNode json = mapper.valueToTree(instance);
        json.fieldNames().forEachRemaining(field -> {
            result.add(field);
        });
        return result;
    }

}
