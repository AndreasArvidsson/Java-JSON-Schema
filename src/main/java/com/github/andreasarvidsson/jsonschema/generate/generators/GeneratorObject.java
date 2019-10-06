package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class GeneratorObject extends GeneratorBase {

    public GeneratorObject() {
        super(Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
