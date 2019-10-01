package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaEnum;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserEnum extends ParserBase {

    public ParserEnum() {
        super(Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = super.parseClass(type);
        final Method jsonValueMethod = ReflectionUtil.getFirstMethod(type, JsonValue.class);
        //Enum with description. Use oneOf array
        if (JsonSchemaEnum.class.isAssignableFrom(type)) {
            addDescriptiveValues(result, jsonValueMethod, (Enum[]) type.getEnumConstants());
        }
        //Basic enum array without description.
        else {
            addSimpleValues(result, jsonValueMethod, (Enum[]) type.getEnumConstants());
        }
        return result;
    }

    private void addDescriptiveValues(final ObjectNode result, final Method jsonValueMethod, final Enum[] enumValues) {
        final ArrayNode oneOfNode = MAPPER.createArrayNode();
        for (final Enum e : enumValues) {
            final ObjectNode node = MAPPER.createObjectNode();
            node.putPOJO("const", getEnumValue(e, jsonValueMethod));
            final String title = ((JsonSchemaEnum) e).getTitle();
            final String description = ((JsonSchemaEnum) e).getDescription();
            if (title != null) {
                node.put(JsonSchemaField.TITLE.toString(), title);
            }
            if (description != null) {
                node.put(JsonSchemaField.DESCRIPTION.toString(), description);
            }
            oneOfNode.add(node);
        }
        result.set("oneOf", oneOfNode);
    }

    private void addSimpleValues(final ObjectNode result, final Method jsonValueMethod, final Enum[] enumValues) {
        final ArrayNode enumNode = MAPPER.createArrayNode();
        for (final Enum e : enumValues) {
            enumNode.addPOJO(getEnumValue(e, jsonValueMethod));
        }
        result.set("enum", enumNode);
    }

    private Object getEnumValue(final Enum e, final Method jsonValueMethod) {
        if (jsonValueMethod != null) {
            try {
                return jsonValueMethod.invoke(e);
            }
            catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return e.toString();
    }

}
