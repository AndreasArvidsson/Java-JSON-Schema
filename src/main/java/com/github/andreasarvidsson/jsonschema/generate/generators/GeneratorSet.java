package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorSet extends GeneratorArrayBase implements GeneratorCollectionInterface {

    public GeneratorSet(final Generators generators) {
        super(generators);
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Class valueType) {
        return super.parseUniqueArrayClass(type, valueType);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
