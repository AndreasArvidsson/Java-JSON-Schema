package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.ClassDefinitions;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Parsers {

    private final Map<Class, Parser> simpleParsers = new IdentityHashMap();
    private final Map<Class, Parser> customParsers;
    private final ParserClass classParser;
    private final ClassDefinitions classDefinitions;

    public Parsers(
            final boolean autoRangeNumbers,
            final Map<Class, Parser> customParsers,
            final ClassDefinitions classDefinitions) {
        this.customParsers = customParsers;
        this.classDefinitions = classDefinitions;
        this.classParser = new ParserClass(this);

        addSimples(autoRangeNumbers);
    }

    public ObjectNode parseClass(final Class type) {
        if (simpleParsers.containsKey(type)) {
            return simpleParsers.get(type).parseClass(type);
        }
        if (!classDefinitions.has(type)) {
            final ObjectNode classNode = createClassNode(type);
            classDefinitions.add(type, classNode);
        }
        return classDefinitions.getRef(type);
    }

    public void parseField(final Field field, final ObjectNode target) {
        final Class type = field.getType();
        if (simpleParsers.containsKey(type)) {
            simpleParsers.get(type).parseField(field, target);
            return;
        }
        if (customParsers.containsKey(type)) {
            customParsers.get(type).parseField(field, target);
            return;
        }
        classParser.parseField(field, target);
    }

    public String getDefType(final Class type) {
        return classDefinitions.getType(type);
    }

    private ObjectNode createClassNode(final Class type) {
        if (customParsers.containsKey(type)) {
            return customParsers.get(type).parseClass(type);
        }
        return classParser.parseClass(type);
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
