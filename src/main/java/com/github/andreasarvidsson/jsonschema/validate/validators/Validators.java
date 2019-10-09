package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.ReflectionUtil;
import com.github.andreasarvidsson.jsonschema.TypeCategories;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class Validators {

    private final Map<Class, Validator> defaultValidators = new IdentityHashMap();
    private final Map<Class, Validator> customValidators;
    private final Validator validatorClass, validatorArray, validatorCollection, validatorMap, validatorNothing;

    public Validators(final Map<Class, Validator> customValidators) {
        this.customValidators = customValidators;
        this.validatorClass = new ValidatorClass(this);
        this.validatorArray = new ValidatorArray(this);
        this.validatorCollection = new ValidatorCollection(this);
        this.validatorMap = new ValidatorMap(this);
        this.validatorNothing = new ValidatorNothing();
        addDefaults();
    }

    public void validateClass(final List<Error> errors, final String path, final Object instance) {
        if (defaultValidators.containsKey(instance.getClass())) {
            return;
        }
        getValidator(instance.getClass()).validateClass(errors, path, instance);
    }

    public void validateSchema(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        getValidator(instance.getClass()).validateSchema(errors, path, instance, jsonSchema);
    }

    private Validator getValidator(final Class type) {
        if (defaultValidators.containsKey(type)) {
            return defaultValidators.get(type);
        }
        if (type.isArray()) {
            return validatorArray;
        }
        if (Map.class.isAssignableFrom(type)) {
            return validatorMap;
        }
        if (Collection.class.isAssignableFrom(type)) {
            return validatorCollection;
        }
        if (ReflectionUtil.hasMethod(type, JsonValue.class)) {
            final Method method = ReflectionUtil.getFirstMethod(type, JsonValue.class);
            return new ValidatorJsonValue(getValidator(method.getReturnType()), method);
        }
        if (customValidators.containsKey(type)) {
            return customValidators.get(type);
        }
        if (type.isEnum()) {
            return validatorNothing;
        }
        return validatorClass;
    }

    private void addDefaults() {
        addDefaults(new ValidatorInteger(), TypeCategories.INTEGERS);
        addDefaults(new ValidatorNumber(), TypeCategories.NUMBERS);
        addDefaults(new ValidatorString(), TypeCategories.STRINGS);
        defaultValidators.put(Object.class, validatorNothing);
    }

    private void addDefaults(final Validator validator, final Collection<Class> types) {
        types.stream().forEach(type -> {
            defaultValidators.put(type, validator);
        });
    }
}
