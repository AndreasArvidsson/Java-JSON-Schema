package com.github.andreasarvidsson.jsonschema.generate.generators;

import com.github.andreasarvidsson.jsonschema.generate.JsonSchemaField;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidssonas Arvidsson
 */
public class GeneratorObject extends GeneratorBase {

    public GeneratorObject() {
        super(Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION
        ));
    }

}
