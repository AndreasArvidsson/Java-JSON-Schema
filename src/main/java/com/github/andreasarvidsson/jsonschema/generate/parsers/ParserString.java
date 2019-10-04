package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class ParserString extends ParserBase {

    public ParserString() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MIN_LENGTH,
                JsonSchemaField.MAX_LENGTH,
                JsonSchemaField.PATTERN,
                JsonSchemaField.FORMAT
        ));
    }

}
