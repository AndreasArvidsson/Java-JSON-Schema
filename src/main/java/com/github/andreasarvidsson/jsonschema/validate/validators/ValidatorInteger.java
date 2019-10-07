package com.github.andreasarvidsson.jsonschema.validate.validators;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorInteger extends ValidatorNumberBase {

    @Override
    protected int compareTo(final Object instance, final String value) {
        return Long.compare(((Number) instance).longValue(), Long.parseLong(value));
    }

    @Override
    protected boolean multipleOf(final Object instance, final String value) {
        return ((Number) instance).longValue() % Long.parseLong(value) == 0;
    }

    @Override
    protected Object getArgument(final String value) {
        return Long.parseLong(value);
    }

}
