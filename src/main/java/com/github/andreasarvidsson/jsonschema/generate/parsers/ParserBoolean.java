package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserBoolean extends ParserBase {

    public ParserBoolean() {
        super(JsonType.BOOLEAN, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
