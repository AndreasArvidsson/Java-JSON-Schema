package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.validate.validators.Validators;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class JsonSchemaValidator {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final Validators validators = new Validators();

    public ValidationReport validate(final Object instance) {
        final List<Error> errors = new ArrayList();
        validators.validate(errors, "instance", instance);
        return new ValidationReport(errors);
    }

    public <T> ValidationReport validate(final InputStream is, final Class<T> type) throws IOException {
        return validate(MAPPER.readValue(is, type));
    }

    public <T> ValidationReport validate(final JsonNode node, Class<T> type) throws IOException {
        return validate(MAPPER.treeToValue(node, type));
    }

}
