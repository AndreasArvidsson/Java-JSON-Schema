package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserString extends ParserBase {

    public ParserString() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_LENGTH,
                JsonSchemaField.MAX_LENGTH,
                JsonSchemaField.PATTERN
        ));
    }

}
