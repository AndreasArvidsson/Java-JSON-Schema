package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Parser {

    public ObjectNode parseClass(final Class type);

    public Set<JsonSchemaField> getAllowedSchemaFields();

}
