package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
@JsonPropertyOrder({"success", "propertyPath", "errors"})
public class ValidationReport {

    private final static ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    private final static ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();
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
            return WRITER.writeValueAsString(this);
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
