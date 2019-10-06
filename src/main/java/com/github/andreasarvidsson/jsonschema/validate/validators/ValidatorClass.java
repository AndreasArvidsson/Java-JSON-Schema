package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorClass implements Validator {

    private final Validators validators;

    public ValidatorClass(final Validators validators) {
        this.validators = validators;
    }

    @Override
    public void validateClass(final List<Error> errors, final String path, final Object instance) {
        final ValidatorClassResultWrapper wrapper = new ValidatorClassResultWrapper();
        validateClassFields(errors, path, instance, instance.getClass(), wrapper);
        //TODO        
        //Validate dependencies
        //Validate combinations
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
    }

    private void validateClassFields(
            final List<Error> errors, final String path, final Object instance,
            final Class type, final ValidatorClassResultWrapper wrapper) {
        //Validate super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            validateClassFields(errors, path, instance, superType, wrapper);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String fieldName = ReflectionUtil.getFieldName(field);
            final Object fieldValue = ReflectionUtil.getFieldValue(field, instance);
            final String fieldPath = PropertyPath.append(path, fieldName);

            final JsonSchema[] jsonSchemas = field.getAnnotationsByType(JsonSchema.class);
            for (final JsonSchema jsonSchema : jsonSchemas) {
                addSchema(errors, path, instance, fieldPath, fieldName, fieldValue, wrapper, jsonSchema);
            }

            if (fieldValue != null) {
                wrapper.fieldNames.add(fieldName);
                validators.validateClass(errors, fieldPath, fieldValue);
            }
        }
    }

    private void addSchema(
            final List<Error> errors, final String path, final Object instance, final String fieldPath, final String fieldName, final Object fieldInstance,
            final ValidatorClassResultWrapper wrapper, final JsonSchema jsonSchema) {
        if (jsonSchema.combining() == JsonSchema.Combining.NONE) {
            if (fieldInstance == null) {
                validateIsRequired(errors, path, instance, fieldName, jsonSchema);
            }
            else {
                validators.validateSchema(errors, fieldPath, fieldInstance, jsonSchema);
            }
        }
        else {
            wrapper.addCombining(fieldPath, jsonSchema);
        }
    }

    private void validateIsRequired(final List<Error> errors, final String path, final Object instance, final String fieldName, final JsonSchema jsonSchema) {
        if (jsonSchema.required()) {
            errors.add(new Error(
                    path,
                    JsonSchemaField.Disabled.REQUIRED.toString(),
                    fieldName,
                    String.format("Requires property '%s'", fieldName),
                    jsonSchema,
                    instance
            ));
        }
    }

}
