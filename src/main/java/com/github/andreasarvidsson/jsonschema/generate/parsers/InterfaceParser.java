package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import java.util.Set;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public interface InterfaceParser {

    public ObjectNode parseClass(final Class type);

    public Set<JsonSchemaField> getAllowedSchemaFields();

}
