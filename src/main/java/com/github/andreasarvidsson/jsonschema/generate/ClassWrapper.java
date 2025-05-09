package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassWrapper {

    public final String name;
    public ObjectNode classNode;
    public List<ObjectNode> references = new ArrayList<>();

    public ClassWrapper(final Class<?> type) {
        this.name = type.getSimpleName();
    }

}
