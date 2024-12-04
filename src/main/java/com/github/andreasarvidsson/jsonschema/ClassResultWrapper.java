package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassResultWrapper {

    public final Set<String> propertyNames = new HashSet<>();
    public final Map<String, JsonSchema> dependencies = new HashMap<>();
    public final Map<Combining, Map<Long, List<ClassCombiningWrapper>>> combinations = new HashMap<>();
    public final Map<String, Map<Combining, List<ClassCombiningWrapper>>> ownProperty = new LinkedHashMap<>();
    public ArrayNode required;
    public ObjectNode properties;
    public Set<String> dependencyPropertyNames;

    public void addCombining(
            final String path, final String propertyName,
            final JsonSchema jsonSchema, final Object instance) {
        addCombining(
                getCombiningGroup(jsonSchema),
                new ClassCombiningWrapper(path, propertyName, jsonSchema, instance));
    }

    public void addCombining(
            final String propertyName,
            final JsonSchema jsonSchema, final ObjectNode propertyNode) {
        addCombining(
                getCombiningGroup(jsonSchema),
                new ClassCombiningWrapper(propertyName, jsonSchema, propertyNode));
    }

    private void addCombining(final long combiningGroup, final ClassCombiningWrapper wrapper) {
        if (combiningGroup == 0) {
            addOwnProperty(wrapper.jsonSchema.combining(), wrapper.propertyName, wrapper);
        } else {
            addCombination(wrapper.jsonSchema.combining(), combiningGroup, wrapper);
        }
    }

    private void addOwnProperty(final Combining combining, final String propertyName,
            final ClassCombiningWrapper wrapper) {
        final Map<Combining, List<ClassCombiningWrapper>> map = getOwnPropertyMap(propertyName);
        if (!map.containsKey(combining)) {
            map.put(combining, new ArrayList<>());
        }
        map.get(combining).add(wrapper);
    }

    private void addCombination(final Combining combining, final long group, final ClassCombiningWrapper wrapper) {
        final Map<Long, List<ClassCombiningWrapper>> map = getCombiningMap(combining);
        if (!map.containsKey(group)) {
            map.put(group, new ArrayList<>());
        }
        map.get(group).add(wrapper);
    }

    private long getCombiningGroup(final JsonSchema jsonSchema) {
        // Calculate group ID for non given.
        long combiningGroup = jsonSchema.combiningGroup();

        // Default value. Generate value,
        if (combiningGroup == Long.MIN_VALUE) {
            return -1 - size(jsonSchema.combining());
        }
        // Given faulty value.
        else if (combiningGroup < 0) {
            throw new RuntimeException(String.format(
                    "Invalid value '%s' for JsonSchema.combiningGroup. Please enter >= 0 values only",
                    combiningGroup));
        }
        return combiningGroup;
    }

    private int size(final Combining combining) {
        return getCombiningMap(combining).size();
    }

    private Map<Long, List<ClassCombiningWrapper>> getCombiningMap(final Combining combining) {
        if (combinations.containsKey(combining)) {
            return combinations.get(combining);
        }
        final Map<Long, List<ClassCombiningWrapper>> map = new LinkedHashMap<>();
        combinations.put(combining, map);
        return map;
    }

    private Map<Combining, List<ClassCombiningWrapper>> getOwnPropertyMap(final String propertyName) {
        if (ownProperty.containsKey(propertyName)) {
            return ownProperty.get(propertyName);
        }
        final Map<Combining, List<ClassCombiningWrapper>> map = new LinkedHashMap<>();
        ownProperty.put(propertyName, map);
        return map;
    }

}
