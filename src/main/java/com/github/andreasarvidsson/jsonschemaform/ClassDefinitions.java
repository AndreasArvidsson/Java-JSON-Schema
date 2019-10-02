package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassDefinitions {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<Class, ClassWrapper> classes = new HashMap();

    public boolean has(final Class type) {
        return classes.containsKey(type);
    }

    public void add(final Class type, final ObjectNode node) {
        final ClassWrapper wrapper = new ClassWrapper();
        wrapper.name = type.getSimpleName();
        wrapper.classNode = node;

        classes.put(type, wrapper);
    }

    public ObjectNode getRef(final Class type) {
        final ObjectNode refNode = MAPPER.createObjectNode();
        final ClassWrapper wrapper = classes.get(type);
        wrapper.references.add(refNode);
        return refNode;
    }

    public String getType(final Class type) {
        if (!classes.containsKey(type)) {
            return null;
        }
        return classes.get(type).classNode.get("type").asText();
    }

    public void update(final ObjectNode schemaNode) {
        final ObjectNode definitions = MAPPER.createObjectNode();
        for (final ClassWrapper wrapper : classes.values()) {
            //Use reference. Add class node to definitions list.
            if (wrapper.references.size() > 1) {
                definitions.set(wrapper.name, wrapper.classNode);
                for (final ObjectNode node : wrapper.references) {
                    node.put("$ref", "#/definitions/" + wrapper.name);
                }
            }
            //Replace reference node with class node.
            else {
                setAllAbsent(wrapper.references.get(0), wrapper.classNode);
            }
        }

        //Add definitions
        if (definitions.size() > 0) {
            schemaNode.set("definitions", definitions);
        }
    }

    private void setAllAbsent(final ObjectNode target, final JsonNode source) {
        final Iterator<Map.Entry<String, JsonNode>> it = source.fields();
        while (it.hasNext()) {
            final Map.Entry<String, JsonNode> e = it.next();
            if (!target.has(e.getKey())) {
                target.set(e.getKey(), e.getValue());
            }
        }
    }

}
