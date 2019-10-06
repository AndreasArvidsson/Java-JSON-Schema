package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorArray implements Validator {

    private final Validators validators;

    public ValidatorArray(final Validators validators) {
        this.validators = validators;
    }

    @Override
    public void validate(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        final int length = Array.getLength(instance);
        validateMinItems(errors, path, instance, jsonSchema, length);
        validateMaxItems(errors, path, instance, jsonSchema, length);
        validate(errors, path, instance);
    }

    @Override
    public void validate(final List<Error> errors, final String path, final Object instance) {
        for (int i = 0; i < Array.getLength(instance); ++i) {
            validators.validate(errors, PropertyPath.append(path, i), Array.get(instance, i));
        }
    }

    private void validateMinItems(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final int length) {
        if (!Objects.equals(jsonSchema.minItems(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.MIN_ITEMS))) {
            if (length < jsonSchema.minItems()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MIN_ITEMS.toString(),
                        jsonSchema.minItems(),
                        String.format("Does not meet minimum length of %d", jsonSchema.minItems()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

    private void validateMaxItems(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema, final int length) {
        if (!Objects.equals(jsonSchema.maxItems(), JsonSchemaUtil.getDefaultValue(JsonSchemaField.MAX_ITEMS))) {
            if (length > jsonSchema.maxItems()) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.MAX_ITEMS.toString(),
                        jsonSchema.maxItems(),
                        String.format("Does not meet maximum length of %d", jsonSchema.maxItems()),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

}