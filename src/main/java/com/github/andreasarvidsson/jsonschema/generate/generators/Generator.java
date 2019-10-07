package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.JsonSchema;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Generator {

    public ObjectNode parseClass(final Class type);

    public void addFields(final Class type, final ObjectNode target);

    public void addFields(final Class type, final ObjectNode target, final JsonSchema jsonSchema);

}
