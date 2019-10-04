package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaEnum;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorEnum extends GeneratorBase {

    public GeneratorEnum() {
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

    private void addDescriptiveValues(
            final ObjectNode result, final Method jsonValueMethod,
            final Enum[] enumValues) {
        final ArrayNode oneOfNode = MAPPER.createArrayNode();
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
            final Enum[] enumValues) {
        final ArrayNode enumNode = MAPPER.createArrayNode();
        for (final Enum e : enumValues) {
            enumNode.addPOJO(getEnumValue(e, jsonValueMethod));
        }
        result.set(JsonSchemaField.Disabled.ENUM.toString(), enumNode);
    }

    private JsonNode createDesc(final Object value, final String title, final String description) {
        final ObjectNode result = MAPPER.createObjectNode();
        result.putPOJO(JsonSchemaField.Disabled.CONST.toString(), value);
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
