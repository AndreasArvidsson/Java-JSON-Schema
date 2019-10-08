package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
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
public class ValidatorClassResultWrapper {

    final Set<String> fieldNames = new HashSet();
    final Map<String, JsonSchema> dependencies = new HashMap();
    final Map<Combining, Map<Long, List<ValidatorClassCombiningWrapper>>> combinations = new HashMap();
    final Map<String, Map<Combining, List<ValidatorClassCombiningWrapper>>> ownProperty = new LinkedHashMap();

    public void addCombining(
            final String path, final String fieldName,
            final JsonSchema jsonSchema, final Object instance) {
        final long combiningGroup = getCombiningGroup(jsonSchema);
        final ValidatorClassCombiningWrapper wrapper = new ValidatorClassCombiningWrapper(
                path, fieldName, instance, jsonSchema
        );
        if (combiningGroup == 0) {
            addOwnProperty(jsonSchema.combining(), fieldName, wrapper);
        }
        else {
            addCombination(jsonSchema.combining(), combiningGroup, wrapper);
        }
    }

    private void addOwnProperty(final Combining combining, final String fieldName, final ValidatorClassCombiningWrapper wrapper) {
        final Map<Combining, List<ValidatorClassCombiningWrapper>> map = getOwnPropertyMap(fieldName);
        if (!map.containsKey(combining)) {
            map.put(combining, new ArrayList());
        }
        map.get(combining).add(wrapper);
    }

    private void addCombination(final Combining combining, final long group, final ValidatorClassCombiningWrapper wrapper) {
        final Map<Long, List<ValidatorClassCombiningWrapper>> map = getCombiningMap(combining);
        if (!map.containsKey(group)) {
            map.put(group, new ArrayList());
        }
        map.get(group).add(wrapper);
    }

    private long getCombiningGroup(final JsonSchema jsonSchema) {
        //Calculate group ID for non given.
        long combiningGroup = jsonSchema.combiningGroup();

        //Default value. Generate value,
        if (combiningGroup == Long.MIN_VALUE) {
            return -1 - size(jsonSchema.combining());
        }
        //Given faulty value.
        else if (combiningGroup < 0) {
            throw new RuntimeException(String.format(
                    "Invalid value '%s' for JsonSchema.combiningGroup. Please enter >= 0 values only",
                    combiningGroup
            ));
        }
        return combiningGroup;
    }

    private int size(final Combining combining) {
        return getCombiningMap(combining).size();
    }

    private Map<Long, List<ValidatorClassCombiningWrapper>> getCombiningMap(final Combining combining) {
        if (combinations.containsKey(combining)) {
            return combinations.get(combining);
        }
        final Map<Long, List<ValidatorClassCombiningWrapper>> map = new LinkedHashMap();
        combinations.put(combining, map);
        return map;
    }

    private Map<Combining, List<ValidatorClassCombiningWrapper>> getOwnPropertyMap(final String fieldName) {
        if (ownProperty.containsKey(fieldName)) {
            return ownProperty.get(fieldName);
        }
        final Map<Combining, List<ValidatorClassCombiningWrapper>> map = new LinkedHashMap();
        ownProperty.put(fieldName, map);
        return map;
    }

}
