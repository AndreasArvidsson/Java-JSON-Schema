package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
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
        final ObjectNode result = parser.parseClass(type);
        final String pattern = getPattern(type);
        if (pattern != null) {
            result.put(JsonSchemaField.PATTERN.toString(), pattern);
        }
        return result;
    }

    @Override
    public Set<JsonSchemaField> getAllowedSchemaFields() {
        return parser.getAllowedSchemaFields();
    }

    private String getPattern(final Class classType) {
        final Constructor cons = ReflectionUtil.getFirstConstructor(classType, JsonCreator.class);
        if (cons != null) {
            if (cons.isAnnotationPresent(Pattern.class)) {
                if (parser.getAllowedSchemaFields().contains(JsonSchemaField.PATTERN)) {
                    final Pattern patternAnot = (Pattern) cons.getAnnotation(Pattern.class);
                    return patternAnot.regexp();
                }
            }
        }
        return null;
    }

}
