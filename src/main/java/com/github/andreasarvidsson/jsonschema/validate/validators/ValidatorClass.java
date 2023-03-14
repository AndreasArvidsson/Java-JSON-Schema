package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.ClassCombiningWrapper;
import com.github.andreasarvidsson.jsonschema.ClassResultWrapper;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
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
        final ClassResultWrapper wrapper = new ClassResultWrapper();
        parseClassFields(errors, path, instance, instance.getClass(), wrapper);
        validateDependencies(errors, path, instance, wrapper);
        validateCombinations(errors, path, instance, wrapper);
    }

    @Override
    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
    }

    private void parseClassFields(
            final List<Error> errors, final String path, final Object instance,
            final Class type, final ClassResultWrapper wrapper) {
        //Validate super classes first.
        final Class superType = type.getSuperclass();
        if (superType != null) {
            parseClassFields(errors, path, instance, superType, wrapper);
        }
        for (final Field field : type.getDeclaredFields()) {
            if (ReflectionUtil.ignoreField(field)) {
                continue;
            }
            final String propertyName = ReflectionUtil.getPropertyName(field);
            final Object propertyValue = ReflectionUtil.getFieldValue(field, instance);
            final String propertyPath = PropertyPath.append(path, propertyName);

            final JsonSchema[] jsonSchemas = field.getAnnotationsByType(JsonSchema.class);
            for (final JsonSchema jsonSchema : jsonSchemas) {
                addSchema(errors, path, instance, propertyPath, propertyName, propertyValue, wrapper, jsonSchema);
            }

            if (propertyValue != null) {
                wrapper.propertyNames.add(propertyName);
                validators.validateClass(errors, propertyPath, propertyValue);
            }
        }
    }

    private void addSchema(
            final List<Error> errors, final String path, final Object instance,
            final String propertyPath, final String propertyName, final Object propertyInstance,
            final ClassResultWrapper wrapper, final JsonSchema jsonSchema) {
        if (jsonSchema.combining() == JsonSchema.Combining.NONE) {
            if (propertyInstance == null) {
                validateIsRequired(errors, path, instance, propertyName, jsonSchema);
            }
            else {
                if (jsonSchema.dependencies().length > 0) {
                    wrapper.dependencies.put(propertyName, jsonSchema);
                }
                validators.validateSchema(errors, propertyPath, propertyInstance, jsonSchema);
            }
        }
        else {
            wrapper.addCombining(propertyPath, propertyName, jsonSchema, propertyInstance);
        }
    }

    private void validateCombinations(
            final List<Error> errors, final String path,
            final Object instance, final ClassResultWrapper wrapper) {
        wrapper.combinations.entrySet().forEach(e -> {
            final Map<Integer, List<Error>> errorMap = validateCombinations(
                    path, instance, wrapper, e.getValue().values(), e.getKey() == Combining.ANY_OF
            );
            addCombinationErrors(errors, path, e.getKey(), e.getValue().size(), errorMap);
        });
        wrapper.ownProperty.values().forEach(map -> {
            map.entrySet().forEach(e -> {
                final Map<Integer, List<Error>> errorMap = validateCombinations(
                        path, instance, wrapper, e.getValue(), e.getKey() == Combining.ANY_OF
                );
                addCombinationErrors(errors, path, e.getKey(), e.getValue().size(), errorMap);
            });
        });
    }

    private Map<Integer, List<Error>> validateCombinations(
            final String path, final Object instance, final ClassResultWrapper wrapper,
            final List<ClassCombiningWrapper> combinations, final boolean onlyOne) {
        final Map<Integer, List<Error>> res = new LinkedHashMap<>();
        int i = 0;
        for (final ClassCombiningWrapper combination : combinations) {
            final List<Error> errors = new ArrayList<>();
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
            final String path, final Object instance, final ClassResultWrapper wrapper,
            final Collection<List<ClassCombiningWrapper>> groups, final boolean onlyOne) {
        final Map<Integer, List<Error>> res = new LinkedHashMap<>();
        int i = 0;
        for (final List<ClassCombiningWrapper> groupCombinations : groups) {
            final List<Error> groupErrors = new ArrayList<>();
            for (final ClassCombiningWrapper combination : groupCombinations) {
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
            final ClassResultWrapper wrapper,
            final ClassCombiningWrapper combination) {
        if (combination.instance == null) {
            validateIsRequired(errors, path, instance, combination.propertyName, combination.jsonSchema);
        }
        else {
            validateDependencies(errors, path, instance, wrapper.propertyNames, combination.propertyName, combination.jsonSchema);
            validators.validateSchema(errors, combination.path, combination.instance, combination.jsonSchema);
        }
    }

    private void addCombinationErrors(final List<Error> errors, final String path,
            final Combining combining, final int nrSchemas, final Map<Integer, List<Error>> errorMap) {
        final int matched = nrSchemas - errorMap.size();
        switch (combining) {
            case ANY_OF:
                if (matched == 0) {
                    errors.add(Error.anyOf(path, new ValidationSubReport(path, combining, nrSchemas, matched, errorMap)));
                }
                break;
            case ONE_OF:
                if (matched != 1) {
                    errors.add(Error.oneOf(path, new ValidationSubReport(path, combining, nrSchemas, matched, errorMap)));
                }
                break;
            case ALL_OF:
                if (matched != nrSchemas) {
                    errors.add(Error.allOf(path, new ValidationSubReport(path, combining, nrSchemas, matched, errorMap)));
                }
                break;
        }
    }

    private void validateIsRequired(final List<Error> errors, final String path, final Object instance, final String propertyName, final JsonSchema jsonSchema) {
        if (jsonSchema.required()) {
            errors.add(Error.requires(path, jsonSchema, propertyName, instance));
        }
    }

    private void validateDependencies(final List<Error> errors, final String path, final Object instance, final ClassResultWrapper wrapper) {
        wrapper.dependencies.entrySet().forEach(e -> {
            validateDependencies(errors, path, instance, wrapper.propertyNames, e.getKey(), e.getValue());
        });
    }

    private void validateDependencies(
            final List<Error> errors, final String path, final Object instance,
            final Set<String> propertyNames, final String propertyName, final JsonSchema jsonSchema) {
        for (final String depName : jsonSchema.dependencies()) {
            if (!propertyNames.contains(depName)) {
                errors.add(Error.dependencies(path, jsonSchema, depName, PropertyPath.append(path, propertyName), instance));
            }
        }
    }

}
