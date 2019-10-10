package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Type;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorSet extends GeneratorArrayBase implements GeneratorCollectionInterface {

    public GeneratorSet(final Generators generators) {
        super(generators);
    }

    @Override
    public ObjectNode parseCollectionClass(final Class type, final Type valueType) {
        return super.parseUniqueArrayClass(type, valueType);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        return parseCollectionClass(type, Object.class);
    }

}
