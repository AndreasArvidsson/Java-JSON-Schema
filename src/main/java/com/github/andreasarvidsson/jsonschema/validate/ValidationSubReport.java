package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.PropertyPath;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidationSubReport {

    public final int matched;
    public final int nrSchemas;
    public final List<ValidationSubSchema> failedSchemas;

    public ValidationSubReport(
            final String path,
            final Combining combining, final int nrSchemas,
            final int matched, final Map<Integer, List<Error>> reports) {
        this.nrSchemas = nrSchemas;
        this.matched = matched;
        this.failedSchemas = new ArrayList();
        reports.entrySet().forEach(e -> {
            failedSchemas.add(new ValidationSubSchema(
                    PropertyPath.append(PropertyPath.append(path, combining.toString()), e.getKey()),
                    e.getValue()
            ));
        });
    }

    public static class ValidationSubSchema {

        public final String property;
        public final List<Error> errors;

        public ValidationSubSchema(final String property, final List<Error> errors) {
            this.property = property;
            this.errors = errors;
        }

    }

}
