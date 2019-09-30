package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class BoolTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchemaGenerator gen = new JsonSchemaGenerator();
    private final String classTitle = "My bool class";
    private final String classDesc = "This is a bool class";
    private final String fieldTitle = "My bool field";
    private final String fieldDesc = "This is a bool field";

    @Test
    public void testBool() {
        AssertJson.assertEquals(
                getExpected("boolean"),
                gen.create(BoolClass1.class)
        );
    }

    @Test
    public void testBoo2() {
        AssertJson.assertEquals(
                getExpected("boolean, null"),
                gen.create(BoolClass2.class)
        );
    }

    private JsonNode getExpected(final String type) {
        final JsonBuilder b = new JsonBuilder();
        b.addField("title", classTitle);
        b.addField("description", classDesc);
        
        final ObjectNode value = mapper.createObjectNode();
        
        value.put("title", fieldTitle);
        value.put("description", fieldDesc);
        value.put("type", type);
        b.addProperty("value", value);
        return b.build();
    }

    @JsonSchema(
            title = classTitle,
            description = classDesc
    )
    class BoolClass1 {

        @JsonSchema(
                title = fieldTitle,
                description = fieldDesc
        )
        boolean value;
        
        
                @JsonSchema(
                title = "woho",
                description = fieldDesc
        )
        Boolean value2;
                
                       
                @JsonSchema(
                title = "woho",
                description = fieldDesc
        )
        Boolean value3;
                
                       
                @JsonSchema(
                title = "woho",
                description = fieldDesc
        )
        boolean value4;
        
    }

    @JsonSchema(
            title = classTitle,
            description = classDesc
    )
    class BoolClass2 {

        @JsonSchema(
                title = fieldTitle,
                description = fieldDesc
        )
        Boolean value;
    }

}
