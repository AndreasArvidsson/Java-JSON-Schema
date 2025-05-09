package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorArray extends GeneratorArrayBase {

    public GeneratorArray(final Generators generators) {
        super(generators);
    }

    @Override
    public ObjectNode parseClass(final Class<?> type) {
        return super.parseArrayClass(type, type.getComponentType());
    }

}
