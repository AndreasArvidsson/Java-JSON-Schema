package com.github.andreasarvidsson.jsonschema.generate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassDefinitions {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, ClassWrapper> classes = new HashMap<>();

    public boolean has(final Class type, final Map<String, Type> args) {
        return classes.containsKey(getKey(type, args));
    }

    public void add(final Class type, final Map<String, Type> args, final ClassWrapper wrapper) {
        classes.put(getKey(type, args), wrapper);
    }

    private String getKey(final Class type, final Map<String, Type> args) {
        final String classKey = type.getTypeName();
        if (args != null) {
            final String argsKey = args.keySet()
                    .stream()
                    .sorted()
                    .map(k -> String.format("%s=%s", k, args.get(k).getTypeName()))
                    .collect(Collectors.joining(", "));
            return String.format("%s(%s)", classKey, argsKey);
        }
        return classKey;
    }

    public ObjectNode getRef(final Class type, final Map<String, Type> args) {
        final ObjectNode refNode = MAPPER.createObjectNode();
        final ClassWrapper wrapper = classes.get(getKey(type, args));
        if (wrapper == null) {
            throw new RuntimeException("Class not found in definitions: " + getKey(type, args));
        }
        wrapper.references.add(refNode);
        return refNode;
    }

    public void update(final ObjectNode schemaNode) {
        final ObjectNode definitions = MAPPER.createObjectNode();
        for (final ClassWrapper wrapper : classes.values()) {
            // Use reference. Add class node to definitions list.
            if (wrapper.references.size() > 1) {
                definitions.set(wrapper.name, wrapper.classNode);
                for (final ObjectNode node : wrapper.references) {
                    node.put("$ref", "#/definitions/" + wrapper.name);
                }
            }
            // Replace reference node with class node.
            else {
                setAllAbsent(wrapper.references.get(0), wrapper.classNode);
            }
        }

        // Add definitions
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
