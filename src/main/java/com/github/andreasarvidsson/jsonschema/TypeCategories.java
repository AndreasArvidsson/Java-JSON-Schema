package com.github.andreasarvidsson.jsonschema;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class TypeCategories {

    public final static Set<Class> INTEGERS = new HashSet<>(Arrays.asList(
            byte.class,
            Byte.class,
            short.class,
            Short.class,
            int.class,
            Integer.class,
            long.class,
            Long.class,
            BigInteger.class));

    public final static Set<Class> NUMBERS = new HashSet<>(Arrays.asList(
            float.class,
            Float.class,
            double.class,
            Double.class,
            BigDecimal.class));

    public final static Set<Class> BOOLEANS = new HashSet<>(Arrays.asList(
            boolean.class,
            Boolean.class));

    public final static Set<Class> CHARACTERS = new HashSet<>(Arrays.asList(
            char.class,
            Character.class));

    public final static Set<Class> STRINGS = new HashSet<>(Arrays.asList(
            String.class,
            CharSequence.class,
            UUID.class,
            Pattern.class));

}
