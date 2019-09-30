package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
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
