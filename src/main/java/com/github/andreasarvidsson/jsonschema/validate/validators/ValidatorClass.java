package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import com.github.andreasarvidsson.jsonschema.validate.ValidationSubReport;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        validateDependencies(errors, path, instance, wrapper);
        validateCombinations(errors, path, instance, wrapper);
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
                if (jsonSchema.dependencies().length > 0) {
                    wrapper.dependencies.put(fieldName, jsonSchema);
                }
                validators.validateSchema(errors, fieldPath, fieldInstance, jsonSchema);
            }
        }
        else {
            wrapper.addCombining(fieldPath, fieldName, jsonSchema, fieldInstance);
        }
    }

    private void validateCombinations(final List<Error> errors, final String path, final Object instance, final ValidatorClassResultWrapper wrapper) {
        wrapper.combinations.entrySet().forEach(e -> {
            final Map<Integer, List<Error>> errorMap = validateCombinations(path, instance, wrapper, e.getValue().values(), e.getKey() == Combining.ANY_OF);
            addCombinationErrors(errors, path, e.getKey(), e.getValue().size(), errorMap);
        });
        wrapper.ownProperty.values().forEach(map -> {
            map.entrySet().forEach(e -> {
                final Map<Integer, List<Error>> errorMap = validateCombinations(path, instance, wrapper, e.getValue(), e.getKey() == Combining.ANY_OF);
                addCombinationErrors(errors, path, e.getKey(), e.getValue().size(), errorMap);
            });
        });
    }

    private Map<Integer, List<Error>> validateCombinations(
            final String path, final Object instance,
            final ValidatorClassResultWrapper wrapper,
            final List<ValidatorClassCombiningWrapper> combinations,
            final boolean onlyOne) {
        final Map<Integer, List<Error>> res = new LinkedHashMap();
        int i = 0;
        for (final ValidatorClassCombiningWrapper combination : combinations) {
            final List<Error> errors = new ArrayList();
            validateCombination(errors, path, instance, wrapper, combination);
            //This combination has errors
            if (!errors.isEmpty()) {
                res.put(i, errors);
            }
            //Only require one valid combination.
            else if (onlyOne) {
                return res;
            }
            ++i;
        }
        return res;
    }

    private Map<Integer, List<Error>> validateCombinations(
            final String path, final Object instance,
            final ValidatorClassResultWrapper wrapper,
            final Collection<List<ValidatorClassCombiningWrapper>> groups,
            final boolean onlyOne) {
        final Map<Integer, List<Error>> res = new LinkedHashMap();
        int i = 0;
        for (final List<ValidatorClassCombiningWrapper> groupCombinations : groups) {
            final List<Error> groupErrors = new ArrayList();
            for (final ValidatorClassCombiningWrapper combination : groupCombinations) {
                validateCombination(groupErrors, path, instance, wrapper, combination);
            }
            //This group has errors
            if (!groupErrors.isEmpty()) {
                res.put(i, groupErrors);
            }
            //Only require one valid group.
            else if (onlyOne) {
                return res;
            }
            ++i;
        }
        return res;
    }

    private void validateCombination(
            final List<Error> errors,
            final String path, final Object instance,
            final ValidatorClassResultWrapper wrapper,
            final ValidatorClassCombiningWrapper combination) {
        if (combination.instance == null) {
            validateIsRequired(errors, path, instance, combination.fieldName, combination.jsonSchema);
        }
        else {
            validateDependencies(errors, path, instance, wrapper.fieldNames, combination.fieldName, combination.jsonSchema);
            validators.validateSchema(errors, combination.path, combination.instance, combination.jsonSchema);
        }
    }

    private void addCombinationErrors(final List<Error> errors, final String path,
            final Combining combining, final int nrSchemas, final Map<Integer, List<Error>> errorMap) {
        final int matched = nrSchemas - errorMap.size();
        switch (combining) {
            case ANY_OF:
                if (matched == 0) {
                    errors.add(new Error(
                            path,
                            combining.toString(),
                            new ValidationSubReport(path, combining, nrSchemas, matched, errorMap),
                            String.format("Does not match at least one schema among %d", nrSchemas)
                    ));
                }
                break;
            case ONE_OF:
                if (matched != 1) {
                    errors.add(new Error(
                            path,
                            combining.toString(),
                            new ValidationSubReport(path, combining, nrSchemas, matched, errorMap),
                            String.format("Does not match exactly one schema (matched %d / %d)", matched, nrSchemas)
                    ));
                }
                break;
            case ALL_OF:
                if (matched != nrSchemas) {
                    errors.add(new Error(
                            path,
                            combining.toString(),
                            new ValidationSubReport(path, combining, nrSchemas, matched, errorMap),
                            String.format("Does not match all required schemas (matched only %d out of %d)", matched, nrSchemas)
                    ));
                }
                break;
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

    private void validateDependencies(final List<Error> errors, final String path, final Object instance, final ValidatorClassResultWrapper wrapper) {
        wrapper.dependencies.entrySet().forEach(e -> {
            validateDependencies(errors, path, instance, wrapper.fieldNames, e.getKey(), e.getValue());
        });
    }

    private void validateDependencies(
            final List<Error> errors, final String path, final Object instance,
            final Set<String> fieldNames, final String fieldName, final JsonSchema jsonSchema) {
        for (final String depFieldName : jsonSchema.dependencies()) {
            if (!fieldNames.contains(depFieldName)) {
                errors.add(new Error(
                        path,
                        JsonSchemaField.Disabled.DEPENDENCIES.toString(),
                        depFieldName,
                        String.format("Property %s not found, required by %s", depFieldName, PropertyPath.append(path, fieldName)),
                        jsonSchema,
                        instance
                ));
            }
        }
    }

}
