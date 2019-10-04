package com.github.andreasarvidsson.jsonschema.generate.parsers;

import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class ParserObject extends ParserBase {

    public ParserObject() {
        super(Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
