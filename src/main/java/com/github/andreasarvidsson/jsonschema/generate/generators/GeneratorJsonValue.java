package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import java.lang.reflect.Method;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorJsonValue implements Generator {

    private final Generator parser;

    public GeneratorJsonValue(final Generators parsers, final Class type) {
        final Method jsonValueMethod = ReflectionUtil.getFirstMethod(type, JsonValue.class);
        final Class returnType = jsonValueMethod.getReturnType();
        this.parser = parsers.getGenerator(returnType);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parser.parseClass(type);
    }

    @Override
    public void addFields(final Class type, final ObjectNode target) {
        parser.addFields(type, target);
    }

    @Override
    public void addFields(final Class type, final ObjectNode target, final JsonSchema jsonSchema) {
        parser.addFields(type, target, jsonSchema);
    }

}
