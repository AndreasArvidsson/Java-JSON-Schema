package com.github.andreasarvidsson.jsonschema.validate.validators;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.validate.Error;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.github.andreasarvidsson.jsonschema.generate.generators.Generator;

/**
 *
 * @author Andreas Arvidsson
 */
public class Validators {

    private final Map<Class, Validator> defaultValidators = new IdentityHashMap();

    private final ValidatorClass validatorClass;

    public Validators() {
        addDefaults();
        validatorClass = new ValidatorClass(this);
    }

    public void validate(final List<Error> violations, final String path, final Object instance) {
        validatorClass.validate(violations, path, instance);
    }

    public void validate(final List<Error> errors, final String path, final Object instance, final JsonSchema jsonSchema) {
        final Class type = instance.getClass();

        if (defaultValidators.containsKey(type)) {
            defaultValidators.get(type).validate(errors, path, instance, jsonSchema);
            return;
        }

        throw new RuntimeException(String.format("Unknown type %s", type.getTypeName()));
//
////        if (type.isArray()) {
////
////        }
////        if (Map.class.isAssignableFrom(type)) {
////            return parserMap;
////        }
////        if (Set.class.isAssignableFrom(type)) {
////            return parserSet;
////        }
////        //Collection that is not map or set.
////        if (Collection.class.isAssignableFrom(type)) {
////            return parserCollection;
////        }
////        if (type.isEnum()) {
////            return parserEnum;
////        }
////              if (customParsers.containsKey(type)) {
////            return customParsers.get(type);
////        }
    }

    private void addDefaults() {
        addDefaults(new ValidatorInteger(), Arrays.asList(
                byte.class,
                Byte.class,
                short.class,
                Short.class,
                int.class,
                Integer.class,
                long.class,
                Long.class,
                BigInteger.class
        ));
//        addSimples(new ParserNumber(), Arrays.asList(
//                float.class,
//                Float.class,
//                double.class,
//                Double.class,
//                BigDecimal.class
//        ));
//        addSimples(new ParserBoolean(), Arrays.asList(
//                boolean.class,
//                Boolean.class
//        ));
//        addSimples(new ParserChar(), Arrays.asList(
//                char.class,
//                Character.class
//        ));
//        addSimples(new ParserString(), Arrays.asList(
//                String.class,
//                CharSequence.class,
//                UUID.class
//        ));
//        simpleParsers.put(Object.class, new ParserObject());
    }

    private void addDefaults(final Validator validator, final List<Class> types) {
        types.stream().forEach(type -> {
            defaultValidators.put(type, validator);
        });
    }
}
