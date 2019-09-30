package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ParserBase implements Parser {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonType jsonType;
    protected final Set<JsonSchemaField> schemaFields;

    public ParserBase(final JsonType jsonType, final Collection<JsonSchemaField> schemaFields) {
        this.jsonType = jsonType;
        this.schemaFields = new HashSet(schemaFields);
    }

    public void addType(final Field field, final ObjectNode node) {
        if (JsonSchemaUtil.isRequired(field)) {
            addType(node);
        }
        else {
            addTypeNull(node);
        }
    }

    public void addType(final Class type, final ObjectNode node) {
        if (JsonSchemaUtil.isRequired(type)) {
            addType(node);
        }
        else {
            addTypeNull(node);
        }
    }

    @Override
    public ObjectNode parse(final Class type) {
        final ObjectNode res = MAPPER.createObjectNode();
        addType(type, res);
        JsonSchemaUtil.addFields(res, type, schemaFields);
        return res;
    }

    public ObjectNode parseRoot(final Class type) {
        final ObjectNode res = MAPPER.createObjectNode();
        addType(res);
        JsonSchemaUtil.addFields(res, type, schemaFields);
        return res;
    }

    private void addType(final ObjectNode node) {
        node.put("type", jsonType.toString());
    }

    private void addTypeNull(final ObjectNode node) {
        node.put("type", String.format("%s, null", jsonType.toString()));
    }

}
