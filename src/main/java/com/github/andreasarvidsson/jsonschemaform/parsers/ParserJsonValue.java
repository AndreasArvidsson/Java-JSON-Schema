package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserJsonValue implements Parser {

    private final Parser parser;

    public ParserJsonValue(final Parsers parsers, final Class type) {
        final Method jsonValueMethod = ReflectionUtil.getFirstMethod(type, JsonValue.class);
        final Class returnType = jsonValueMethod.getReturnType();
        this.parser = parsers.getParser(returnType);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = parser.parseClass(type);
        final String pattern = getPattern(type);
        if (pattern != null) {
            result.put("pattern", pattern);
        }
        return result;
    }

    @Override
    public ObjectNode parseClassField(final Field field) {
        return parseClass(field.getType());
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
