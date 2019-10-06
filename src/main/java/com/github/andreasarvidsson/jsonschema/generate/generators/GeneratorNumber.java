package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorNumber extends GeneratorBase {

    public GeneratorNumber() {
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
