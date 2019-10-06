package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorClass extends GeneratorBase {

    private final Generators generators;

    public GeneratorClass(final Generators generators) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
        this.generators = generators;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode classNode = super.parseClass(type);
        classNode.put(JsonSchemaField.Disabled.ADDITIONAL_PROPERTIES.toString(), ReflectionUtil.hasAnyGetterAndAnySetter(type));

        final GeneratorClassResultWrapper wrapper = new GeneratorClassResultWrapper();
        parseClassFields(type, wrapper);

        if (wrapper.required.size() > 0) {
            classNode.set(JsonSchemaField.Disabled.REQUIRED.toString(), wrapper.required);
        }
        if (wrapper.properties.size() > 0) {
            classNode.set(JsonSchemaField.Disabled.PROPERTIES.toString(), wrapper.properties);
        }
        if (wrapper.dependencies.size() > 0) {
            classNode.set(JsonSchemaField.Disabled.DEPENDENCIES.toString(), wrapper.dependencies);
        }

        addCombinations(classNode, Combining.ANY_OF, wrapper.getMap(Combining.ANY_OF).values());
        addCombinations(classNode, Combining.ONE_OF, wrapper.getMap(Combining.ONE_OF).values());
        addCombinations(classNode, Combining.ALL_OF, wrapper.getMap(Combining.ALL_OF).values());

        validateDependencies(type, wrapper.fieldNames, wrapper.dependencyFieldNames);

        return classNode;
    }

    private void parseClassFields(final Class type, final GeneratorClassResultWrapper wrapper) {
        final List<Field> fields = new ArrayList();
        getFields(fields, type);
        final JsonPropertyOrder order = ReflectionUtil.getFirstAnotation(type, JsonPropertyOrder.class);
        if (order != null) {
            sortFields(fields, order);
        }

        for (final Field field : fields) {
            final String fieldName = ReflectionUtil.getFieldName(field);
            wrapper.fieldNames.add(fieldName);

            final ObjectNode fieldNode = generators.parseClassField(field);

            //Add field anotations
            final Set<JsonSchemaField> allowedFields = generators.getAllowedSchemaFields(field.getType());

            //Add schema anotations
            addSchemas(allowedFields, wrapper, fieldNode, fieldName, field);

            wrapper.properties.set(fieldName, fieldNode);
        }
    }

    private void sortFields(final List<Field> fields, final JsonPropertyOrder order) {
        final Map<String, Integer> orderMap = new HashMap();
        for (final String fieldName : order.value()) {
            orderMap.put(fieldName, orderMap.size());
        }
        fields.sort((final Field o1, final Field o2) -> {
            final String n1 = ReflectionUtil.getFieldName(o1);
            final String n2 = ReflectionUtil.getFieldName(o2);
            final Integer i1 = orderMap.get(n1);
            final Integer i2 = orderMap.get(n2);
            if (i1 != null && i2 != null) {
                return i1 - i2;
            }
            if (i1 != null) {
                return -1;
            }
            if (i2 != null) {
                return 1;
            }
            if (order.alphabetic()) {
                return n1.compareTo(n2);
            }
            return 0;
        });
    }

    private void getFields(final List<Field> result, final Class type) {
        //Parse super classes first.
        if (type.getSuperclass() != null) {
            getFields(result, type.getSuperclass());
        }
        for (final Field field : type.getDeclaredFields()) {
            if (!ReflectionUtil.ignoreField(field)) {
                result.add(field);
            }
        }
    }

    private void addSchemas(
            final Set<JsonSchemaField> allowedFields, final GeneratorClassResultWrapper wrapper,
            final ObjectNode fieldNode, final String fieldName, final Field field) {
        final Class type = field.getType();
        final JsonSchema[] jsonSchemas = field.getAnnotationsByType(JsonSchema.class);
        final int requiredSize = wrapper.required.size();
        for (final JsonSchema jsonSchema : jsonSchemas) {
            addSchema(allowedFields, wrapper, fieldNode, type, fieldName, jsonSchema);
        }
        //This field is a primitive and it had no required anot.
        if (type.isPrimitive() && wrapper.required.size() == requiredSize) {
            wrapper.required.add(fieldName);
        }
    }

    private void addSchema(
            final Set<JsonSchemaField> allowedFields, final GeneratorClassResultWrapper wrapper,
            ObjectNode fieldNode, final Class type,
            final String fieldName, final JsonSchema jsonSchema) {

        final boolean isCombining = jsonSchema.combining() != Combining.NONE;
        boolean addCombining = false;

        if (isCombining) {
            fieldNode = MAPPER.createObjectNode();
        }

        //Apply required
        if (jsonSchema.required()) {
            //Required for combining is added separetly in next step.
            if (isCombining) {
                addCombining = true;
            }
            else {
                wrapper.required.add(fieldName);
            }
        }

        //Apply dependencies
        if (jsonSchema.dependencies().length > 0) {
            final ArrayNode dependenciesNode = MAPPER.createArrayNode();
            for (final String depName : jsonSchema.dependencies()) {
                if (depName.equals(fieldName)) {
                    throw new RuntimeException(String.format(
                            "Error in field '%s', Can't have dependency to itself",
                            fieldName)
                    );
                }
                dependenciesNode.add(depName);
                wrapper.dependencyFieldNames.add(depName);
            }
            //Dependencies for combining is added separetly in next step.
            if (isCombining) {
                addCombining = true;
            }
            else {
                wrapper.dependencies.set(fieldName, dependenciesNode);
            }
        }

        //Add other fields to node.
        JsonSchemaUtil.addFields(type, fieldNode, allowedFields, jsonSchema);

        //Only add crossfield node if it contains any data.
        if (isCombining && (fieldNode.size() > 0 || addCombining)) {
            wrapper.addCombining(fieldName, fieldNode, jsonSchema);
        }
    }

    private void addCombinations(
            final ObjectNode classNode,
            final Combining combining,
            final Collection<List<GeneratorClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = getUniqueNames(groupCombinings);
        final boolean singleField = uniqueNames.size() == 1;
        final ArrayNode combiningArray = MAPPER.createArrayNode();
        final Map<String, ArrayNode> ownPropertyMap = new HashMap();

        //Each in this list is a new group.
        for (final List<GeneratorClassCombiningWrapper> groupCombining : groupCombinings) {
            final ObjectNode combiningGroup = MAPPER.createObjectNode();
            //Each in this list is a new jsonSchema but in the same group.
            for (final GeneratorClassCombiningWrapper combiningWrapper : groupCombining) {
                //Parent level schemas.
                if (combiningWrapper.jsonSchema.required()) {
                    addRequired(combiningGroup, combiningWrapper.fieldName);
                }
                if (combiningWrapper.jsonSchema.dependencies().length > 0) {
                    addDependencies(combiningGroup, combiningWrapper.fieldName, combiningWrapper.jsonSchema.dependencies());
                }

                //Property level schemas.
                if (combiningWrapper.node.size() > 0) {
                    if (combiningWrapper.ownProperty) {
                        addOwnProperty(ownPropertyMap, combiningWrapper);
                    }
                    else {
                        addProperties(combiningGroup, combiningWrapper.fieldName, combiningWrapper.node, singleField);
                    }
                }
            }

            if (combiningGroup.size() > 0) {
                combiningArray.add(combiningGroup);
            }
        }

        if (combiningArray.size() > 0) {
            if (singleField) {
                addToProperty(classNode, uniqueNames.iterator().next(), combining, combiningArray);
            }
            else {
                classNode.set(combining.toString(), combiningArray);
            }
        }

        ownPropertyMap.forEach((fieldName, arrayNode) -> {
            addToProperty(classNode, fieldName, combining, arrayNode);
        });
    }

    private void addToProperty(
            final ObjectNode classNode, final String fieldName,
            final Combining combining, final ArrayNode combinationArray) {
        ((ObjectNode) classNode.get(JsonSchemaField.Disabled.PROPERTIES.toString()).get(fieldName))
                .set(combining.toString(), combinationArray);
    }

    private void addOwnProperty(final Map<String, ArrayNode> ownPropertyMap, final GeneratorClassCombiningWrapper combiningWrapper) {
        final ObjectNode ownNode = MAPPER.createObjectNode();
        addProperties(ownNode, combiningWrapper.fieldName, combiningWrapper.node, true);
        if (!ownPropertyMap.containsKey(combiningWrapper.fieldName)) {
            ownPropertyMap.put(combiningWrapper.fieldName, MAPPER.createArrayNode());
        }
        ownPropertyMap.get(combiningWrapper.fieldName).add(ownNode);
    }

    private void addRequired(final ObjectNode target, final String value) {
        if (!target.has(JsonSchemaField.Disabled.REQUIRED.toString())) {
            target.set(JsonSchemaField.Disabled.REQUIRED.toString(), MAPPER.createArrayNode());
        }
        ((ArrayNode) target.get(JsonSchemaField.Disabled.REQUIRED.toString())).add(value);
    }

    private void addDependencies(final ObjectNode target, final String fieldName, final String[] values) {
        ObjectNode dependencies = (ObjectNode) target.get(JsonSchemaField.Disabled.DEPENDENCIES.toString());
        if (dependencies == null) {
            dependencies = MAPPER.createObjectNode();
            target.set(JsonSchemaField.Disabled.DEPENDENCIES.toString(), dependencies);
        }
        dependencies.putPOJO(fieldName, values);
    }

    private void addProperties(
            final ObjectNode target, final String fieldName,
            final ObjectNode node, final boolean ownProperty) {
        if (ownProperty) {
            target.setAll(node);
        }
        else {
            ObjectNode propertiesNode = (ObjectNode) target.get(JsonSchemaField.Disabled.PROPERTIES.toString());
            if (propertiesNode == null) {
                propertiesNode = MAPPER.createObjectNode();
                target.set(JsonSchemaField.Disabled.PROPERTIES.toString(), propertiesNode);
            }
            ObjectNode fieldNode = (ObjectNode) propertiesNode.get(fieldName);
            if (fieldNode == null) {
                fieldNode = MAPPER.createObjectNode();
                propertiesNode.set(fieldName, fieldNode);
            }
            fieldNode.setAll(node);
        }
    }

    private void validateDependencies(final Class type, final Set<String> fieldNames, final Set<String> dependencyFieldNames) {
        for (final String depFieldName : dependencyFieldNames) {
            if (!fieldNames.contains(depFieldName)) {
                throw new RuntimeException(String.format(
                        "Dependency on missing field '%s' not possible for class '%s'",
                        depFieldName, type.getTypeName())
                );
            }
        }
    }

    private Set<String> getUniqueNames(final Collection<List<GeneratorClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = new HashSet();
        groupCombinings.forEach(list -> {
            list.forEach(c -> {
                uniqueNames.add(c.fieldName);
            });
        });
        return uniqueNames;
    }

}
