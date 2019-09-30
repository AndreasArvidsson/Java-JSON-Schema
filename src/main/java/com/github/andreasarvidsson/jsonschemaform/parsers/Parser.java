package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Parser {

    public ObjectNode parse(final Class type);

}
