package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
@JsonPropertyOrder({"success", "propertyPath", "errors"})
public class ValidationReport {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private List<Error> errors = null;
    public final String propertyPath = "instance";

    ValidationReport(final List<Error> errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public List<Error> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }
        catch (JsonProcessingException ex) {
            return null;
        }
    }

}
