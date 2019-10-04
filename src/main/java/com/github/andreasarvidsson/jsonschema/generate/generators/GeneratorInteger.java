package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorInteger extends GeneratorBase {

    private final static Map<Class, Long> MIN_VALUES = new HashMap();
    private final static Map<Class, Long> MAX_VALUES = new HashMap();

    private static void addRange(final Class type, final long min, final long max) {
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
        super(JsonType.INTEGER, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MINIMUM,
                JsonSchemaField.MAXIMUM,
                JsonSchemaField.EXCLUSIVE_MINIMUM,
                JsonSchemaField.EXCLUSIVE_MAXIMUM,
                JsonSchemaField.MULTIPLE_OF
        ));
        this.autoRangeNumbers = autoRangeNumbers;
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = super.parseClass(type);
        if (autoRangeNumbers && MIN_VALUES.containsKey(type)) {
            result.put(JsonSchemaField.MINIMUM.toString(), MIN_VALUES.get(type));
            result.put(JsonSchemaField.MAXIMUM.toString(), MAX_VALUES.get(type));
        }
        return result;
    }

}
