package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.generate.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorBoolean extends GeneratorBase {

    public GeneratorBoolean() {
        super(JsonType.BOOLEAN, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
