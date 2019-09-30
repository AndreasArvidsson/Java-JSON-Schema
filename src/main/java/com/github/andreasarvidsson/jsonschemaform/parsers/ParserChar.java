package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserChar extends ParserBase {

    public ParserChar() {
        super(JsonType.STRING, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
