package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.generate.ClassDefinitions;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Andreas Arvidsson
 */
public class Parsers {

    private final Map<Class, InterfaceParser> simpleParsers = new IdentityHashMap();
    private final Map<Class, InterfaceParser> customParsers;
    private final InterfaceParser parserClass, parserArray, parserEnum;
    private final InterfaceParserCollection parserMap, parserSet, parserCollection;
    private final ClassDefinitions classDefinitions;

    public Parsers(
            final boolean autoRangeNumbers,
            final Map<Class, InterfaceParser> customParsers,
            final ClassDefinitions classDefinitions) {
        this.customParsers = customParsers;
        this.classDefinitions = classDefinitions;
        this.parserClass = new ParserClass(this);
        this.parserMap = new ParserMap(this);
        this.parserArray = new ParserArray(this);
        this.parserSet = new ParserSet(this);
        this.parserCollection = new ParserCollection(this);
        this.parserEnum = new ParserEnum();
        addSimples(autoRangeNumbers);
    }

    public ObjectNode parseClass(final Class type) {
        return parseClass(type, null);
    }

    public ObjectNode parseClassField(final Field field) {
        return parseClass(field.getType(), field);
    }

    public Set<JsonSchemaField> getAllowedSchemaFields(final Class type) {
        return getParser(type).getAllowedSchemaFields();
    }

    public String getDefType(final Class type) {
        return classDefinitions.getType(type);
    }

    public InterfaceParser getParser(final Class type) {
        InterfaceParser parser = getSimpleParser(type);
        if (parser != null) {
            return parser;
        }
        parser = (InterfaceParser) getCollectionParser(type);
        if (parser != null) {
            return parser;
        }
        return getAdvancedParser(type);
    }

    private ObjectNode parseClass(final Class type, final Field field) {
        //First check if we already parsed this class;
        if (classDefinitions.has(type)) {
            return classDefinitions.getRef(type);
        }

        //Dont use references / definiitions for simple or collection types.
        final InterfaceParser simpleParser = getSimpleParser(type);
        if (simpleParser != null) {
            return simpleParser.parseClass(type);
        }

        final InterfaceParserCollection collectionParser = getCollectionParser(type);
        if (collectionParser != null) {
            final Class valueType = field != null ? ReflectionUtil.getGenericValueType(field) : Object.class;
            return collectionParser.parseCollectionClass(type, valueType);
        }

        final ObjectNode classNode = getAdvancedParser(type).parseClass(type);
        classDefinitions.add(type, classNode);
        return classDefinitions.getRef(type);
    }

    private InterfaceParser getSimpleParser(final Class type) {
        if (type.isArray()) {
            return parserArray;
        }
        if (simpleParsers.containsKey(type)) {
            return simpleParsers.get(type);
        }
        if (!type.isEnum() && ReflectionUtil.hasMethod(type, JsonValue.class)) {
            return new ParserJsonValue(this, type);
        }
        return null;
    }

    private InterfaceParserCollection getCollectionParser(final Class type) {
        if (Map.class.isAssignableFrom(type)) {
            return parserMap;
        }
        if (Set.class.isAssignableFrom(type)) {
            return parserSet;
        }
        //Collection that is not map or set.
        if (Collection.class.isAssignableFrom(type)) {
            return parserCollection;
        }
        return null;
    }

    private InterfaceParser getAdvancedParser(final Class type) {
        if (customParsers.containsKey(type)) {
            return customParsers.get(type);
        }
        if (type.isEnum()) {
            return parserEnum;
        }
        return parserClass;
    }

    private void addSimples(final boolean autoRangeNumbers) {
        addSimples(new ParserInteger(autoRangeNumbers), Arrays.asList(
                byte.class,
                Byte.class,
                short.class,
                Short.class,
                int.class,
                Integer.class,
                long.class,
                Long.class,
                BigInteger.class
        ));
        addSimples(new ParserNumber(), Arrays.asList(
                float.class,
                Float.class,
                double.class,
                Double.class,
                BigDecimal.class
        ));
        addSimples(new ParserBoolean(), Arrays.asList(
                boolean.class,
                Boolean.class
        ));
        addSimples(new ParserChar(), Arrays.asList(
                char.class,
                Character.class
        ));
        addSimples(new ParserString(), Arrays.asList(
                String.class,
                CharSequence.class,
                UUID.class
        ));
        simpleParsers.put(Object.class, new ParserObject());
    }

    private void addSimples(final InterfaceParser parser, final List<Class> types) {
        types.stream().forEach(type -> {
            simpleParsers.put(type, parser);
        });
    }

}
