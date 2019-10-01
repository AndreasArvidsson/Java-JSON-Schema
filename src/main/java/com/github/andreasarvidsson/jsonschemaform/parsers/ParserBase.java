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
        addType(type, result);
        JsonSchemaUtil.addFields(type, result, type, schemaFields);
        return result;
    }

    @Override
    public void parseField(final Field field, final ObjectNode target) {
        JsonSchemaUtil.addFields(field.getType(), target, field, schemaFields);
    }

    private void addType(final Class type, final ObjectNode node) {
        if (jsonType != null) {
            if (JsonSchemaUtil.isRequired(type)) {
                node.put("type", jsonType.toString());
            }
            else {
                node.put("type", String.format("%s, null", jsonType.toString()));
            }
        }
    }

}
