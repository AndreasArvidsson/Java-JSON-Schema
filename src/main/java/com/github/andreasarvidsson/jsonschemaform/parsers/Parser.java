package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Field;

/**
 *
 * @author Andreas Arvidsson
 */
public interface Parser {

    public ObjectNode parseClass(final Class type);

    public void parseField(final Field field, final ObjectNode target);

}
