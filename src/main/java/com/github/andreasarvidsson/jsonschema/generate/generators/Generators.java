package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.generate.ClassDefinitions;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
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
public class Generators {

    private final Map<Class, Generator> simpleGenerators = new IdentityHashMap();
    private final Map<Class, Generator> customGenerators;
    private final Generator generatorClass, generatorArray, generatorEnum;
    private final GeneratorCollectionInterface generatorMap, generatorSet, generatorCollection;
    private final ClassDefinitions classDefinitions;

    public Generators(
            final boolean autoRangeNumbers,
            final Map<Class, Generator> customGenerators,
            final ClassDefinitions classDefinitions) {
        this.customGenerators = customGenerators;
        this.classDefinitions = classDefinitions;
        this.generatorClass = new GeneratorClass(this);
        this.generatorMap = new GeneratorMap(this);
        this.generatorArray = new GeneratorArray(this);
        this.generatorSet = new GeneratorSet(this);
        this.generatorCollection = new GeneratorCollection(this);
        this.generatorEnum = new GeneratorEnum();
        addSimples(autoRangeNumbers);
    }

    public ObjectNode parseClass(final Class type) {
        return parseClass(type, null);
    }

    public ObjectNode parseClassField(final Field field) {
        return parseClass(field.getType(), field);
    }

    public Set<JsonSchemaField> getAllowedSchemaFields(final Class type) {
        return getGenerator(type).getAllowedSchemaFields();
    }

    public String getDefType(final Class type) {
        return classDefinitions.getType(type);
    }

    public Generator getGenerator(final Class type) {
        Generator generator = getSimpleGenerator(type);
        if (generator != null) {
            return generator;
        }
        generator = (Generator) getCollectionGenerator(type);
        if (generator != null) {
            return generator;
        }
        return getAdvancedGenerator(type);
    }

    private ObjectNode parseClass(final Class type, final Field field) {
        //First check if we already parsed this class;
        if (classDefinitions.has(type)) {
            return classDefinitions.getRef(type);
        }

        //Dont use references / definiitions for simple or collection types.
        final Generator simpleGenerator = getSimpleGenerator(type);
        if (simpleGenerator != null) {
            return simpleGenerator.parseClass(type);
        }

        final GeneratorCollectionInterface collectionGenerator = getCollectionGenerator(type);
        if (collectionGenerator != null) {
            final Class valueType = field != null ? ReflectionUtil.getGenericValueType(field) : Object.class;
            return collectionGenerator.parseCollectionClass(type, valueType);
        }

        final ObjectNode classNode = getAdvancedGenerator(type).parseClass(type);
        classDefinitions.add(type, classNode);
        return classDefinitions.getRef(type);
    }

    private Generator getSimpleGenerator(final Class type) {
        if (type.isArray()) {
            return generatorArray;
        }
        if (simpleGenerators.containsKey(type)) {
            return simpleGenerators.get(type);
        }
        if (!type.isEnum() && ReflectionUtil.hasMethod(type, JsonValue.class)) {
            return new GeneratorJsonValue(this, type);
        }
        return null;
    }

    private GeneratorCollectionInterface getCollectionGenerator(final Class type) {
        if (Map.class.isAssignableFrom(type)) {
            return generatorMap;
        }
        if (Set.class.isAssignableFrom(type)) {
            return generatorSet;
        }
        //Collection that is not set.
        if (Collection.class.isAssignableFrom(type)) {
            return generatorCollection;
        }
        return null;
    }

    private Generator getAdvancedGenerator(final Class type) {
        if (customGenerators.containsKey(type)) {
            return customGenerators.get(type);
        }
        if (type.isEnum()) {
            return generatorEnum;
        }
        return generatorClass;
    }

    private void addSimples(final boolean autoRangeNumbers) {
        addSimples(new GeneratorInteger(autoRangeNumbers), Arrays.asList(
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
        addSimples(new GeneratorNumber(), Arrays.asList(
                float.class,
                Float.class,
                double.class,
                Double.class,
                BigDecimal.class
        ));
        addSimples(new GeneratorBoolean(), Arrays.asList(
                boolean.class,
                Boolean.class
        ));
        addSimples(new GeneratorChar(), Arrays.asList(
                char.class,
                Character.class
        ));
        addSimples(new GeneratorString(), Arrays.asList(
                String.class,
                CharSequence.class,
                UUID.class
        ));
        simpleGenerators.put(Object.class, new GeneratorObject());
    }

    private void addSimples(final Generator generator, final List<Class> types) {
        types.stream().forEach(type -> {
            simpleGenerators.put(type, generator);
        });
    }

}
