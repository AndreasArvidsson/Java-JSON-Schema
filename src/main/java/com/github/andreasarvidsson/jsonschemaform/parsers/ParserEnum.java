package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaEnum;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
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
        return parseClass(type, true);
    }

    @Override
    public ObjectNode parseClassField(final Field field) {
        return parseClass(field.getType(), JsonSchemaUtil.isRequired(field));
    }

    private ObjectNode parseClass(final Class type, final boolean isRequired) {
        final ObjectNode result = super.parseClass(type);
        final Method jsonValueMethod = ReflectionUtil.getFirstMethod(type, JsonValue.class);
        //Enum with description. Use oneOf array
        if (JsonSchemaEnum.class.isAssignableFrom(type)) {
            addDescriptiveValues(result, jsonValueMethod, (Enum[]) type.getEnumConstants(), isRequired);
        }
        //Basic enum array without description.
        else {
            addSimpleValues(result, jsonValueMethod, (Enum[]) type.getEnumConstants(), isRequired);
        }
        return result;
    }

    private void addDescriptiveValues(
            final ObjectNode result, final Method jsonValueMethod,
            final Enum[] enumValues, final boolean isRequired) {
        final ArrayNode oneOfNode = MAPPER.createArrayNode();
        if (!isRequired) {
            oneOfNode.add(createDesc(null, null, null));
        }
        for (final Enum e : enumValues) {
            oneOfNode.add(createDesc(
                    getEnumValue(e, jsonValueMethod),
                    ((JsonSchemaEnum) e).getTitle(),
                    ((JsonSchemaEnum) e).getDescription()
            ));
        }
        result.set("oneOf", oneOfNode);
    }

    private void addSimpleValues(
            final ObjectNode result, final Method jsonValueMethod,
            final Enum[] enumValues, final boolean isRequired) {
        final ArrayNode enumNode = MAPPER.createArrayNode();
        if (!isRequired) {
            enumNode.addPOJO(null);
        }
        for (final Enum e : enumValues) {
            enumNode.addPOJO(getEnumValue(e, jsonValueMethod));
        }
        result.set("enum", enumNode);
    }

    private JsonNode createDesc(final Object value, final String title, final String description) {
        final ObjectNode result = MAPPER.createObjectNode();
        result.putPOJO("const", value);
        if (title != null) {
            result.put(JsonSchemaField.TITLE.toString(), title);
        }
        if (description != null) {
            result.put(JsonSchemaField.DESCRIPTION.toString(), description);
        }
        return result;
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
