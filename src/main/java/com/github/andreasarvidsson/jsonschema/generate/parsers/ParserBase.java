package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaUtil;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class ParserBase implements InterfaceParser {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonType jsonType;
    protected final Set<JsonSchemaField> schemaFields;

    public ParserBase(final Collection<JsonSchemaField> schemaFields) {
        this(null, schemaFields);
    }

    public ParserBase(final JsonType jsonType, final Collection<JsonSchemaField> schemaFields) {
        this.jsonType = jsonType;
        this.schemaFields = new HashSet(schemaFields);
    }

    @Override
    public ObjectNode parseClass(final Class type) {
        final ObjectNode result = MAPPER.createObjectNode();
        addType(result);
        JsonSchemaUtil.addFields(type, result, schemaFields);
        return result;
    }

    @Override
    public Set<JsonSchemaField> getAllowedSchemaFields() {
        return schemaFields;
    }

    private void addType(final ObjectNode node) {
        if (jsonType != null) {
            node.put("type", jsonType.toString());
        }
    }

}
