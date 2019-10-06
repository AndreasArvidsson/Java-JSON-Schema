package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public interface GeneratorCollectionInterface extends Generator {

    public ObjectNode parseCollectionClass(final Class type, final Class valueType);

}
