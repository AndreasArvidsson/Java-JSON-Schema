package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserObject extends ParserBase {

    public ParserObject() {
        super(Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
