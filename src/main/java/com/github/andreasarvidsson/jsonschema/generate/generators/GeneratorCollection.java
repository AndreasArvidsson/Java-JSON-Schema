package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorCollection extends GeneratorArrayBase implements GeneratorCollectionInterface {

    public GeneratorCollection(final Generators generators) {
        super(generators);
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        return super.parseArrayClass(type, valueType);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
