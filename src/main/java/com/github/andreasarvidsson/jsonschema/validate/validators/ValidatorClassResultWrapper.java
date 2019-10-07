package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import java.util.ArrayList;
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
    final List<ValidatorClassDependency> dependencies = new ArrayList();
    final Map<Long, List<ValidatorClassCombiningWrapper>> oneOfs = new LinkedHashMap();
    final Map<Long, List<ValidatorClassCombiningWrapper>> anyOfs = new LinkedHashMap();
    final Map<Long, List<ValidatorClassCombiningWrapper>> allOfs = new LinkedHashMap();

    public void addCombining(final String fieldName, final JsonSchema jsonSchema) {
        final long combiningGroup = getCombiningGroup(jsonSchema);
        add(
                jsonSchema.combining(),
                combiningGroup,
                new ValidatorClassCombiningWrapper(fieldName, jsonSchema, combiningGroup == 0)
        );
    }

    public int size(final Combining combining) {
        return getMap(combining).size();
    }

    public Map<Long, List<ValidatorClassCombiningWrapper>> getMap(final Combining combining) {
        switch (combining) {
            case ANY_OF:
                return anyOfs;
            case ONE_OF:
                return oneOfs;
            case ALL_OF:
                return allOfs;
            default:
                return null;
        }
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

    private void add(final Combining combining, final long group, final ValidatorClassCombiningWrapper wrapper) {
        final Map<Long, List<ValidatorClassCombiningWrapper>> map = getMap(combining);
        if (!map.containsKey(group)) {
            map.put(group, new ArrayList());
        }
        map.get(group).add(wrapper);
    }

}
