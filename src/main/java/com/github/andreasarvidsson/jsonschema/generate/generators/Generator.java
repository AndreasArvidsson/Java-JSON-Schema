package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Generator {

    public ObjectNode parseClass(final Class type);

    public Set<JsonSchemaField> getAllowedSchemaFields();

}
