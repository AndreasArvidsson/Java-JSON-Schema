package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class ParserNumber extends ParserBase {

    public ParserNumber() {
        super(JsonType.NUMBER, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MINIMUM,
                JsonSchemaField.MAXIMUM,
                JsonSchemaField.EXCLUSIVE_MINIMUM,
                JsonSchemaField.EXCLUSIVE_MAXIMUM,
                JsonSchemaField.MULTIPLE_OF
        ));
    }

}
