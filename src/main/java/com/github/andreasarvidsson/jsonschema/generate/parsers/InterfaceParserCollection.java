package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public interface InterfaceParserCollection extends InterfaceParser {

    public ObjectNode parseCollectionClass(final Class type, final Class valueType);

}
