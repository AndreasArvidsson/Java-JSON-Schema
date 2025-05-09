package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.ClassCombiningWrapper;
import com.github.andreasarvidsson.jsonschema.ClassResultWrapper;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
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
        super(
                JsonType.OBJECT, Arrays.asList(
                        JsonSchemaField.TITLE,
                        JsonSchemaField.DESCRIPTION,
                        JsonSchemaField.DEPRECATED));
        this.generators = generators;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseClass(type, null);
    }

    public ObjectNode parseClass(final Class type, final Map<String, Type> args) {
        final ClassResultWrapper wrapper = new ClassResultWrapper();
        wrapper.required = MAPPER.createArrayNode();
        wrapper.properties = MAPPER.createObjectNode();
        wrapper.dependencyPropertyNames = new HashSet<>();
        parseClassFields(type, args, wrapper);

        final ObjectNode classNode = super.parseClass(type);
        classNode.put(
                JsonSchemaField.Disabled.ADDITIONAL_PROPERTIES.toString(),
                ReflectionUtil.hasAnyGetterAndAnySetter(type));
        if (wrapper.required.size() > 0) {
            classNode.set(JsonSchemaField.Disabled.REQUIRED.toString(), wrapper.required);
        }
        if (wrapper.dependencies.size() > 0) {
            addDependencies(classNode, wrapper.propertyNames, wrapper.dependencies);
        }
        classNode.set(JsonSchemaField.Disabled.PROPERTIES.toString(), wrapper.properties);
        wrapper.combinations.entrySet().forEach(e -> {
            addCombinations(classNode, wrapper.propertyNames, e.getKey(), e.getValue().values());
        });
        wrapper.ownProperty.values().forEach(map -> {
            map.entrySet().forEach(e -> {
                addOwnProperty(classNode, e.getKey(), e.getValue());
            });
        });
        return classNode;
    }

    private void parseClassFields(final Class type, final Map<String, Type> args, final ClassResultWrapper wrapper) {
        final List<Field> fields = ReflectionUtil.getFieldsInOrder(type);
        for (final Field field : fields) {
            final String propertyName = ReflectionUtil.getPropertyName(field);
            final ObjectNode propertyNode = generators.parseClassField(field, args);
            final Generator generator = generators.getGenerator(field.getType());

            // Add schema anotations
            try {
                addSchemas(generator, wrapper, propertyNode, propertyName, field);
            } catch (final Exception ex) {
                throw new RuntimeException(
                        String.format(
                                "[%s] -> %s", ReflectionUtil.getPath(field), ex.getMessage()));
            }

            wrapper.propertyNames.add(propertyName);
            wrapper.properties.set(propertyName, propertyNode);
        }
    }

    private void addSchemas(
            final Generator generator, final ClassResultWrapper wrapper,
            final ObjectNode propertyNode, final String propertyName, final Field field) {
        final Class type = field.getType();
        final JsonSchema[] jsonSchemas = field.getAnnotationsByType(JsonSchema.class);
        final int requiredSize = wrapper.required.size();
        for (final JsonSchema jsonSchema : jsonSchemas) {
            addSchema(generator, wrapper, propertyNode, type, propertyName, jsonSchema);
        }
        // This field is a primitive and it had no required anot.
        if (type.isPrimitive() && wrapper.required.size() == requiredSize) {
            wrapper.required.add(propertyName);
        }
    }

    private void addSchema(
            final Generator generator, final ClassResultWrapper wrapper,
            ObjectNode target, final Class type,
            final String propertyName, final JsonSchema jsonSchema) {
        final boolean isCombining = jsonSchema.combining() != Combining.NONE;
        boolean addCombining = false;

        if (isCombining) {
            target = MAPPER.createObjectNode();
        }

        if (jsonSchema.required()) {
            // Required for combining is added separetly in later step.
            if (isCombining) {
                addCombining = true;
            } else {
                wrapper.required.add(propertyName);
            }
        }

        if (jsonSchema.dependencies().length > 0) {
            // Dependencies for combining is added separetly in later step.
            if (isCombining) {
                addCombining = true;
            } else {
                wrapper.dependencies.put(propertyName, jsonSchema);
            }
        }

        // Add other fields to node.
        generator.addFields(type, target, jsonSchema);

        // Only add combining node if it contains any data.
        if (isCombining && (target.size() > 0 || addCombining)) {
            wrapper.addCombining(propertyName, jsonSchema, target);
        }
    }

    private void addCombinations(
            final ObjectNode classNode, final Set<String> propertyNames,
            final Combining combining,
            final Collection<List<ClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = getUniqueNames(groupCombinings);
        final boolean singleField = uniqueNames.size() == 1;
        final ArrayNode combiningArray = MAPPER.createArrayNode();

        // Each in this list is a new group.
        for (final List<ClassCombiningWrapper> groupCombining : groupCombinings) {
            final ObjectNode combiningGroup = MAPPER.createObjectNode();
            // Each in this list is a new jsonSchema but in the same group.
            for (final ClassCombiningWrapper combiningWrapper : groupCombining) {
                // Parent level schemas.
                if (combiningWrapper.jsonSchema.required()) {
                    addRequired(combiningGroup, combiningWrapper.propertyName);
                }
                if (combiningWrapper.jsonSchema.dependencies().length > 0) {
                    addDependencies(
                            combiningGroup, propertyNames, combiningWrapper.propertyName, combiningWrapper.jsonSchema);
                }
                // Property level schemas.
                if (combiningWrapper.propertyNode.size() > 0) {
                    if (singleField) {
                        combiningGroup.setAll(combiningWrapper.propertyNode);
                    } else {
                        addProperty(combiningGroup, combiningWrapper.propertyName, combiningWrapper.propertyNode);
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
            } else {
                classNode.set(combining.toString(), combiningArray);
            }
        }
    }

    private void addOwnProperty(
            final ObjectNode classNode,
            final Combining combining,
            final List<ClassCombiningWrapper> combinings) {
        final ArrayNode combiningArray = MAPPER.createArrayNode();
        final String propertyName = combinings.get(0).propertyName;
        // Each in this list is a new jsonSchema and in a different group, but on the
        // same property.
        combinings.forEach(combiningWrapper -> {
            // Parent level schemas are not allowed for own property anotations.
            if (combiningWrapper.jsonSchema.required()) {
                throw new RuntimeException(
                        String.format(
                                "Required is not applicable together with combining group 0. '%s'",
                                propertyName));
            }
            if (combiningWrapper.jsonSchema.dependencies().length > 0) {
                throw new RuntimeException(
                        String.format(
                                "Dependencies is not applicable together with combining group 0. '%s'",
                                propertyName));
            }
            if (combiningWrapper.propertyNode.size() > 0) {
                combiningArray.add(combiningWrapper.propertyNode);
            }
        });
        addToProperty(classNode, propertyName, combining, combiningArray);
    }

    private void addToProperty(
            final ObjectNode classNode, final String propertyName,
            final Combining combining, final ArrayNode combinationArray) {
        ((ObjectNode) classNode
                .get(JsonSchemaField.Disabled.PROPERTIES.toString())
                .get(propertyName))
                .set(combining.toString(), combinationArray);
    }

    private void addRequired(final ObjectNode target, final String value) {
        if (!target.has(JsonSchemaField.Disabled.REQUIRED.toString())) {
            target.set(JsonSchemaField.Disabled.REQUIRED.toString(), MAPPER.createArrayNode());
        }
        ((ArrayNode) target
                .get(JsonSchemaField.Disabled.REQUIRED.toString()))
                .add(value);
    }

    private void addDependencies(
            final ObjectNode target, final Set<String> propertyNames, final String propertyName,
            final JsonSchema jsonSchema) {
        ObjectNode dependencies = (ObjectNode) target.get(JsonSchemaField.Disabled.DEPENDENCIES.toString());
        if (dependencies == null) {
            dependencies = MAPPER.createObjectNode();
            target.set(JsonSchemaField.Disabled.DEPENDENCIES.toString(), dependencies);
        }
        addDependencies(dependencies, propertyNames, propertyName, jsonSchema.dependencies());
    }

    private void addDependencies(
            final ObjectNode target, final Set<String> propertyNames, final Map<String, JsonSchema> dependencies) {
        final ObjectNode dependenciesNode = MAPPER.createObjectNode();
        dependencies.entrySet().forEach(e -> {
            addDependencies(dependenciesNode, propertyNames, e.getKey(), e.getValue().dependencies());
        });
        target.set(JsonSchemaField.Disabled.DEPENDENCIES.toString(), dependenciesNode);
    }

    private void addDependencies(
            final ObjectNode target, final Set<String> propertyNames, final String propertyName,
            final String[] values) {
        final ArrayNode arrNode = MAPPER.createArrayNode();
        for (final String depName : values) {
            if (propertyName.equals(depName)) {
                throw new RuntimeException(
                        String.format(
                                "'%s' can not have dependency to itself",
                                propertyName));
            }
            if (!propertyNames.contains(depName)) {
                throw new RuntimeException(
                        String.format(
                                "'%s' has dependency on missing field '%s'",
                                propertyName, depName));
            }
            arrNode.add(depName);
        }
        target.set(propertyName, arrNode);
    }

    private void addProperty(
            final ObjectNode target, final String propertyName,
            final ObjectNode node) {
        ObjectNode propertiesNode = (ObjectNode) target.get(JsonSchemaField.Disabled.PROPERTIES.toString());
        if (propertiesNode == null) {
            propertiesNode = MAPPER.createObjectNode();
            target.set(JsonSchemaField.Disabled.PROPERTIES.toString(), propertiesNode);
        }
        ObjectNode propertyNode = (ObjectNode) propertiesNode.get(propertyName);
        if (propertyNode == null) {
            propertyNode = MAPPER.createObjectNode();
            propertiesNode.set(propertyName, propertyNode);
        }
        propertyNode.setAll(node);

    }

    private Set<String> getUniqueNames(final Collection<List<ClassCombiningWrapper>> groupCombinings) {
        final Set<String> uniqueNames = new HashSet<>();
        groupCombinings.forEach(list -> {
            list.forEach(c -> {
                uniqueNames.add(c.propertyName);
            });
        });
        return uniqueNames;
    }

}
