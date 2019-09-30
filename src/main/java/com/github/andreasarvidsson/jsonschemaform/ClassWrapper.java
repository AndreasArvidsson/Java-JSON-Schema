package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Arvidsson
 */
public class ClassWrapper {

    public String name;
    public ObjectNode classNode;
    public List<ObjectNode> references = new ArrayList();

}
