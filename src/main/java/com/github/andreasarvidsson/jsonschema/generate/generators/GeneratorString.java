package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorString extends GeneratorBase {

    public GeneratorString() {
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
