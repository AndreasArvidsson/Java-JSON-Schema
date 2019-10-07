package com.github.andreasarvidsson.jsonschema.validate.validators;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorNumber extends ValidatorNumberBase {

    @Override
    protected int compareTo(final Object instance, final String value) {
        return Double.compare(((Number) instance).doubleValue(), Double.parseDouble(value));
    }

    @Override
    protected boolean multipleOf(final Object instance, final String value) {
        return ((Number) instance).doubleValue() % Double.parseDouble(value) == 0;
    }

    @Override
    protected Object getArgument(final String value) {
        return Double.parseDouble(value);
    }

}
