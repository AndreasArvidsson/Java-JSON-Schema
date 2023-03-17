package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.andreasarvidsson.jsonschema.validate.validators.Validator;
import com.github.andreasarvidsson.jsonschema.validate.validators.Validators;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class JsonSchemaValidator {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final Map<Class, Validator> customValidators = new HashMap<>();

    public JsonSchemaValidator addCustomValidator(final Class type, final Validator validator) {
        customValidators.put(type, validator);
        return this;
    }

    public ValidationReport validate(final Object instance) {
        final Validators validators = new Validators(customValidators);
        final List<Error> errors = new ArrayList<>();
        validators.validateClass(errors, "instance", instance);
        return new ValidationReport(errors);
    }

    public ValidationReport validate(final InputStream is, final Class type) throws IOException {
        return validate(MAPPER.readValue(is, type));
    }

    public ValidationReport validate(final JsonNode node, final Class type) throws IOException {
        return validate(MAPPER.treeToValue(node, type));
    }

}
