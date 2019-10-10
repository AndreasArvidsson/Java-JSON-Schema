package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.TypeCategories;
import com.github.andreasarvidsson.jsonschema.generate.ClassDefinitions;
import com.github.andreasarvidsson.jsonschema.generate.ClassWrapper;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class Generators {

    private final Map<Class, Generator> defaultGenerators = new IdentityHashMap();
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
        addDefaults(autoRangeNumbers);
    }

    public ObjectNode parseClass(final Type genericType) {
        //Generic type
        if (genericType instanceof ParameterizedType) {
            final Type[] paramTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            //Use last index instead of [0] for map where value is at [1]
            final Type type = paramTypes[paramTypes.length - 1];

            //Nested collection or map.
            if (type instanceof ParameterizedType) {
                final ParameterizedType paramType = (ParameterizedType) type;
                final Class collectionType = (Class) paramType.getRawType();
                return getCollectionGenerator(collectionType).parseCollectionClass(
                        collectionType,
                        paramType.getActualTypeArguments()[paramType.getActualTypeArguments().length - 1]
                );
            }

            //Not a generic type. Just a class.
            return parseClass((Class) type);
        }

        //Not a generic type. Just a class.
        return parseClass((Class) genericType);
    }

    public ObjectNode parseClass(final Class type) {
        return parseClass(type, null);
    }

    public ObjectNode parseClassField(final Field field) {
        return parseClass(field.getType(), field);
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
            if (field != null && field.getGenericType() instanceof ParameterizedType) {
                //Field has ParameterizedType. Use it.
                if (field.getGenericType() instanceof ParameterizedType) {
                    return collectionGenerator.parseCollectionClass(type, field.getGenericType());
                }
            }
            //Field doesnt have ParameterizedType. Defautl to Object
            else {
                return collectionGenerator.parseCollectionClass(type, Object.class);
            }
        }

        final ClassWrapper wrapper = new ClassWrapper(type);
        //Need to add wrapper to definitions before parsing members in case of circular dependencies.
        classDefinitions.add(type, wrapper);
        wrapper.classNode = getAdvancedGenerator(type).parseClass(type);

        return classDefinitions.getRef(type);
    }

    private Generator getSimpleGenerator(final Class type) {
        if (type.isArray()) {
            return generatorArray;
        }
        if (defaultGenerators.containsKey(type)) {
            return defaultGenerators.get(type);
        }
        if (!type.isEnum() && ReflectionUtil.hasMethod(type, JsonValue.class)) {
            return getGenerator(ReflectionUtil.getFirstMethod(type, JsonValue.class).getReturnType());
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

    private void addDefaults(final boolean autoRangeNumbers) {
        addDefaults(new GeneratorInteger(autoRangeNumbers), TypeCategories.INTEGERS);
        addDefaults(new GeneratorNumber(), TypeCategories.NUMBERS);
        addDefaults(new GeneratorBoolean(), TypeCategories.BOOLEANS);
        addDefaults(new GeneratorChar(), TypeCategories.CHARACTERS);
        addDefaults(new GeneratorString(), TypeCategories.STRINGS);
        defaultGenerators.put(Object.class, new GeneratorObject());
    }

    private void addDefaults(final Generator generator, final Collection<Class> types) {
        types.stream().forEach(type -> {
            defaultGenerators.put(type, generator);
        });
    }

}
