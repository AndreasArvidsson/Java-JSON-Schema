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
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class Generators {

    private final Map<Class, Generator> defaultGenerators = new IdentityHashMap<>();
    private final Map<Class, Generator> customGenerators;
    private final GeneratorClass generatorClass;
    private final Generator generatorArray, generatorEnum;
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

    public ObjectNode parseClass(final Type type) {
        //Parameterized type
        if (type instanceof ParameterizedType) {
            return parseParameterizedType((ParameterizedType)type);
        }

        //Not a parameterized type. Just a class.
        return parseClass((Class) type);
    }

    public ObjectNode parseClass(final Class type) {
        return parseClass(type, null, null);
    }

    public ObjectNode parseClassField(final Field field, final Map<String, Type> args) {
        if (field.getGenericType() instanceof ParameterizedType) {
            return parseParameterizedType((ParameterizedType)field.getGenericType());
        }

        //Type variable. eg: T value;
        if(field.getGenericType() instanceof TypeVariable) {
            final Type arg = getArgument(args, (TypeVariable)field.getGenericType());
            return parseClass((Class)arg, null, null);
        }

        return parseClass(field.getType(), field, args);
    }

    private ObjectNode parseParameterizedType(final ParameterizedType type) {
        final Class rawType = (Class)type.getRawType();
        final Type[] args = type.getActualTypeArguments();
        final GeneratorCollectionInterface collectionGenerator = getCollectionGenerator(rawType);

        if(collectionGenerator != null) {
            //Use last index instead of [0] for map where value is at [1]
            final Type valueType = args[args.length - 1];
            return collectionGenerator.parseCollectionClass(rawType, valueType);
        }

        final TypeVariable<Class>[] params = rawType.getTypeParameters();

        if(args.length != params.length) {
            throw new RuntimeException(String.format("Mismatch between number of arguments and parameters"));
        }

        final Map<String, Type> argsMap = new HashMap<>();

        for (int i = 0; i < args.length; ++i) {
            argsMap.put(params[i].getName(), args[i]);
        }

        //Not a generic type. Just a.
        return parseClass(rawType, null, argsMap);
    }

    public Generator getGenerator(final Class type) {
        Generator generator = getSimpleGenerator(type);
        if (generator != null) {
            return generator;
        }
        generator = getCollectionGenerator(type);
        if (generator != null) {
            return generator;
        }
        return getAdvancedGenerator(type);
    }

    private ObjectNode parseClass(final Class type, final Field field, final Map<String, Type> args) {
        //First check if we already parsed this class;
        if (classDefinitions.has(type, args)) {
            return classDefinitions.getRef(type, args);
        }

        //Dont use references / definiitions for simple or collection types.
        final Generator simpleGenerator = getSimpleGenerator(type);
        if (simpleGenerator != null) {
            return simpleGenerator.parseClass(type);
        }

        final GeneratorCollectionInterface collectionGenerator = getCollectionGenerator(type);
        if (collectionGenerator != null) {
            if (field != null && field.getGenericType() instanceof ParameterizedType) {
                final ParameterizedType paramType = (ParameterizedType)field.getGenericType();
                final Type[] collArgs = paramType.getActualTypeArguments();
                //Use last index instead of [0] for map where value is at [1]
                final Type valueType = collArgs[collArgs.length - 1];
                if(args != null) {
                    //Type variable. eg: <T>
                    if(valueType instanceof TypeVariable) {
                        final Type arg = getArgument(args, (TypeVariable)valueType);
                        return collectionGenerator.parseCollectionClass(type, arg);
                    }
                }
                return collectionGenerator.parseCollectionClass(type, valueType);
            }
            //Field doesnt have ParameterizedType. Default to Object
            else {
                return collectionGenerator.parseCollectionClass(type, Object.class);
            }
        }

        final ClassWrapper wrapper = new ClassWrapper(type);
        //Need to add wrapper to definitions before parsing members in case of circular dependencies.
        classDefinitions.add(type,args, wrapper);

        if(args != null) {
            wrapper.classNode = generatorClass.parseClass(type, args);
        }
        else {
            wrapper.classNode = getAdvancedGenerator(type).parseClass(type);
        }

        return classDefinitions.getRef(type, args);
    }

    private Type getArgument(final Map<String, Type> args, final TypeVariable typeVar) {
        if(!args.containsKey(typeVar.getName())) {
            throw new RuntimeException(String.format("Unknown parameter: '%s'", typeVar.getName()));
        }
        return args.get(typeVar.getName());
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
        //Collection that is not map or set.
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
