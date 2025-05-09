package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorInteger extends GeneratorBase {

    private final static Map<Class<?>, Long> MIN_VALUES = new HashMap<>();
    private final static Map<Class<?>, Long> MAX_VALUES = new HashMap<>();

    private static void addRange(final Class<?> type, final long min, final long max) {
        MIN_VALUES.put(type, min);
        MAX_VALUES.put(type, max);
    }

    static {
        addRange(byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
        addRange(Byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
        addRange(short.class, Short.MIN_VALUE, Short.MAX_VALUE);
        addRange(Short.class, Short.MIN_VALUE, Short.MAX_VALUE);
        addRange(int.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addRange(Integer.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addRange(long.class, Long.MIN_VALUE, Long.MAX_VALUE);
        addRange(Long.class, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private final boolean autoRangeNumbers;

    public GeneratorInteger(final boolean autoRangeNumbers) {
        super(JsonType.INTEGER, GeneratorNumber.FIELDS);
        this.autoRangeNumbers = autoRangeNumbers;
    }

    @Override
    public ObjectNode parseClass(final Class<?> type) {
        final ObjectNode result = super.parseClass(type);
        if (autoRangeNumbers && MIN_VALUES.containsKey(type)) {
            result.put(JsonSchemaField.MINIMUM.toString(), MIN_VALUES.get(type));
            result.put(JsonSchemaField.MAXIMUM.toString(), MAX_VALUES.get(type));
        }
        return result;
    }

    @Override
    public void addFields(final Class<?> type, final ObjectNode target, final JsonSchema jsonSchema) {
        super.addFields(type, target, jsonSchema);
        JsonSchemaUtil.setIntegers(type, target,
                JsonSchemaField.MINIMUM, jsonSchema.minimum(), Long.MIN_VALUE, Long.MAX_VALUE,
                JsonSchemaField.MAXIMUM, jsonSchema.maximum(), Long.MIN_VALUE, Long.MAX_VALUE);
        JsonSchemaUtil.setIntegers(type, target,
                JsonSchemaField.EXCLUSIVE_MINIMUM, jsonSchema.exclusiveMinimum(), Long.MIN_VALUE, Long.MAX_VALUE,
                JsonSchemaField.EXCLUSIVE_MAXIMUM, jsonSchema.exclusiveMaximum(), Long.MIN_VALUE, Long.MAX_VALUE);
        JsonSchemaUtil.setInteger(type, target, JsonSchemaField.MULTIPLE_OF, jsonSchema.multipleOf(), Long.MIN_VALUE,
                Long.MAX_VALUE);
        JsonSchemaUtil.setInteger(type, target, JsonSchemaField.CONST, jsonSchema.constant(), Long.MIN_VALUE,
                Long.MAX_VALUE);
    }

}
