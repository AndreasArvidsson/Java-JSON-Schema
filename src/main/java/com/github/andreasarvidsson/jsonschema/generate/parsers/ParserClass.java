package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.lang.reflect.Field;
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
public class ParserClass extends ParserBase {

    private final Parsers parsers;

    public ParserClass(final Parsers parsers) {
        super(JsonType.OBJECT, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
        this.parsers = parsers;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode classNode = super.parseClass(type);
        classNode.put("additionalProperties", ReflectionUtil.hasAnyGetterAndAnySetter(type));

        final ParserClassResultWrapper wrapper = new ParserClassResultWrapper();
        parseClassFields(type, wrapper);

        if (wrapper.required.size() > 0) {
            classNode.set("required", wrapper.required);
        }
        if (wrapper.properties.size() > 0) {
            classNode.set("properties", wrapper.properties);
        }
        if (wrapper.dependencies.size() > 0) {
            classNode.set("dependencies", wrapper.dependencies);
        }

        addCrossFieldConstraints(classNode, Combining.ANY_OF, wrapper.getMap(Combining.ANY_OF).values());
        addCrossFieldConstraints(classNode, Combining.ONE_OF, wrapper.getMap(Combining.ONE_OF).values());
        addCrossFieldConstraints(classNode, Combining.ALL_OF, wrapper.getMap(Combining.ALL_OF).values());

        validateDependencies(type, wrapper.fieldNames, wrapper.dependencyFieldNames);

        return classNode;
    }

    private void parseClassFields(final Class type, final ParserClassResultWrapper wrapper) {
        //Parse super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            parseClassFields(superType, wrapper);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String fieldName = ReflectionUtil.getFieldName(field);
            wrapper.fieldNames.add(fieldName);

            final ObjectNode fieldNode = parsers.parseClassField(field);

            //Add field anotations
            final Set<JsonSchemaField> allowedFields = parsers.getAllowedSchemaFields(field.getType());

            //Add schema anotations
            addSchemas(allowedFields, wrapper, fieldNode, fieldName, field);

            wrapper.properties.set(fieldName, fieldNode);
        }
    }

    private void addSchemas(
            final Set<JsonSchemaField> allowedFields, final ParserClassResultWrapper wrapper,
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
            final Set<JsonSchemaField> allowedFields, final ParserClassResultWrapper wrapper,
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

    private void addCrossFieldConstraints(
            final ObjectNode classNode,
            final Combining combining,
            final Collection<List<ParserClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = getUniqueNames(groupCombinings);
        final boolean singleField = uniqueNames.size() == 1;
        final ArrayNode combiningArray = MAPPER.createArrayNode();
        final Map<String, ArrayNode> ownPropertyMap = new HashMap();

        //Each in this list is a new group.
        for (final List<ParserClassCombiningWrapper> groupCombining : groupCombinings) {
            final ObjectNode combiningGroup = MAPPER.createObjectNode();
            //Each in this list is a new jsonSchema but in the same group.
            for (final ParserClassCombiningWrapper combiningWrapper : groupCombining) {
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
        ((ObjectNode) classNode.get("properties").get(fieldName))
                .set(combining.toString(), combinationArray);
    }

    private void addOwnProperty(final Map<String, ArrayNode> ownPropertyMap, final ParserClassCombiningWrapper combiningWrapper) {
        final ObjectNode ownNode = MAPPER.createObjectNode();
        addProperties(ownNode, combiningWrapper.fieldName, combiningWrapper.node, true);
        if (!ownPropertyMap.containsKey(combiningWrapper.fieldName)) {
            ownPropertyMap.put(combiningWrapper.fieldName, MAPPER.createArrayNode());
        }
        ownPropertyMap.get(combiningWrapper.fieldName).add(ownNode);
    }

    private void addRequired(final ObjectNode target, final String value) {
        if (!target.has("required")) {
            target.set("required", MAPPER.createArrayNode());
        }
        ((ArrayNode) target.get("required")).add(value);
    }

    private void addDependencies(final ObjectNode target, final String fieldName, final String[] values) {
        ObjectNode dependencies = (ObjectNode) target.get("dependencies");
        if (dependencies == null) {
            dependencies = MAPPER.createObjectNode();
            target.set("dependencies", dependencies);
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
            ObjectNode propertiesNode = (ObjectNode) target.get("properties");
            if (propertiesNode == null) {
                propertiesNode = MAPPER.createObjectNode();
                target.set("properties", propertiesNode);
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

    private Set<String> getUniqueNames(final Collection<List<ParserClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = new HashSet();
        groupCombinings.forEach(list -> {
            list.forEach(c -> {
                uniqueNames.add(c.fieldName);
            });
        });
        return uniqueNames;
    }

}
