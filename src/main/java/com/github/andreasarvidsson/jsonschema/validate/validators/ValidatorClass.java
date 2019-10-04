package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import com.github.andreasarvidsson.jsonschema.validate.Error.Name;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorClass {

    private final Validators validators;

    public ValidatorClass(final Validators validators) {
        this.validators = validators;
    }

    public void validate(final List<Error> errors, final String path, final Object instance) {
        validateClassFields(errors, path, instance, instance.getClass());
    }

    private void validateClassFields(final List<Error> errors, final String path, final Object instance, final Class type) {
        //Validate super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            validateClassFields(errors, path, instance, superType);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String fieldName = ReflectionUtil.getFieldName(field);
            final Object fieldValue = ReflectionUtil.getFieldValue(field, instance);
            final String fieldPath = path + "/" + fieldName;
            final JsonSchema[] jsonSchemas = field.getAnnotationsByType(JsonSchema.class);
            for (final JsonSchema jsonSchema : jsonSchemas) {
                if (jsonSchema.combining() == JsonSchema.Combining.NONE) {
                    if (fieldValue == null) {
                        validateIsRequired(errors, path, fieldName, jsonSchema);
                    }
                    else {
                        validators.validate(errors, fieldPath, fieldValue, jsonSchema);
                    }
                }
            }
        }
    }

    private void validateIsRequired(final List<Error> errors, final String path, final String fieldName, final JsonSchema jsonSchema) {
        if (jsonSchema.required()) {
            errors.add(new Error(
                    path,
                    Name.REQUIRED,
                    fieldName,
                    String.format("Requires property '%s'", fieldName),
                    jsonSchema,
                    null
            ));
        }
    }

}
