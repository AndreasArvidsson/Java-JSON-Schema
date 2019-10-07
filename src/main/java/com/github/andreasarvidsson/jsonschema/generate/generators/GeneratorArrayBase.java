package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class GeneratorArrayBase extends GeneratorBase {

    private final Generators generators;

    protected GeneratorArrayBase(final Generators generators) {
        super(JsonType.ARRAY, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_ITEMS,
                JsonSchemaField.MAX_ITEMS
        ));
        this.generators = generators;
    }

    @Override
    public void addFields(final Class type, final ObjectNode target, final JsonSchema jsonSchema) {
        super.addFields(type, target, jsonSchema);
        JsonSchemaUtil.setIntegers(type, target,
                JsonSchemaField.MIN_ITEMS, jsonSchema.minItems(), 0, Integer.MAX_VALUE,
                JsonSchemaField.MAX_ITEMS, jsonSchema.maxItems(), 1, Integer.MAX_VALUE
        );
    }

    private ObjectNode parseArrayClass(final Class type, final Class valueType, final boolean isUnique) {
        final ObjectNode result = super.parseClass(type);
        if (isUnique) {
            result.put(JsonSchemaField.Disabled.UNIQUE_ITEMS.toString(), true);
        }
        result.set(JsonSchemaField.Disabled.ITEMS.toString(), generators.parseClass(valueType));
        return result;
    }

    protected ObjectNode parseArrayClass(final Class type, final Class valueType) {
        return parseArrayClass(type, valueType, false);
    }

    protected ObjectNode parseUniqueArrayClass(final Class type, final Class valueType) {
        return parseArrayClass(type, valueType, true);
    }

}
