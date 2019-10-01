package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.ClassDefinitions;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.ReflectionUtil;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class Parsers {

    private final Map<Class, Parser> simpleParsers = new IdentityHashMap();
    private final Map<Class, Parser> customParsers;
    private final ParserClass parserClass;
    private final ParserMap parserMap;
    private final ParserArray parserArray;
    private final ParserSet parserSet;
    private final ParserCollection parserCollection;
    private final ClassDefinitions classDefinitions;

    public Parsers(
            final boolean autoRangeNumbers,
            final Map<Class, Parser> customParsers,
            final ClassDefinitions classDefinitions) {
        this.customParsers = customParsers;
        this.classDefinitions = classDefinitions;
        this.parserClass = new ParserClass(this);
        this.parserMap = new ParserMap(this);
        this.parserArray = new ParserArray(this);
        this.parserSet = new ParserSet(this);
        this.parserCollection = new ParserCollection(this);
        addSimples(autoRangeNumbers);
    }

    public ObjectNode parseClass(final Class type) {
        if (simpleParsers.containsKey(type)) {
            return simpleParsers.get(type).parseClass(type);
        }
        final Parser collectionparser = getCollectionParser(type);
        if (collectionparser != null) {
            return collectionparser.parseClass(type);
        }
        //Dont use references / definiitions for simple or collection types. 
        if (!classDefinitions.has(type)) {
            final ObjectNode classNode = createClassNode(type);
            classDefinitions.add(type, classNode);
        }
        return classDefinitions.getRef(type);
    }

    public ObjectNode parseClassField(final Field field) {
        return getParser(field.getType()).parseClassField(field);
    }

    public Set<JsonSchemaField> getAllowedSchemaFields(final Class type) {
        return getParser(type).getAllowedSchemaFields();
    }

    public String getDefType(final Class type) {
        return classDefinitions.getType(type);
    }

    private ObjectNode createClassNode(final Class type) {
        if (customParsers.containsKey(type)) {
            return customParsers.get(type).parseClass(type);
        }
        return parserClass.parseClass(type);
    }

    private Parser getParser(final Class type) {
        if (simpleParsers.containsKey(type)) {
            return simpleParsers.get(type);
        }
        final Parser collectionparser = getCollectionParser(type);
        if (collectionparser != null) {
            return collectionparser;
        }
        if (customParsers.containsKey(type)) {
            return customParsers.get(type);
        }
        return parserClass;
    }

    private Parser getCollectionParser(final Class type) {
        if (ReflectionUtil.isArray(type)) {
            return parserArray;
        }
        if (ReflectionUtil.isMap(type)) {
            return parserMap;
        }
        if (ReflectionUtil.isSet(type)) {
            return parserSet;
        }
        if (ReflectionUtil.isCollection(type)) {
            return parserCollection;
        }
        return null;
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
                CharSequence.class
        ));
        simpleParsers.put(Object.class, new ParserObject());
    }

    private void addSimples(final Parser parser, final List<Class> types) {
        types.stream().forEach(type -> {
            simpleParsers.put(type, parser);
        });
    }

}
