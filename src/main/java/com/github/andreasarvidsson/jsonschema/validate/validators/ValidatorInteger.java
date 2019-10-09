package com.github.andreasarvidsson.jsonschema.validate.validators;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorInteger extends ValidatorNumberBase {

    @Override
    protected int compareTo(final Number instance, final String value) {
        return Long.compare(instance.longValue(), Long.parseLong(value));
    }

    @Override
    protected boolean multipleOf(final Number instance, final String value) {
        return instance.longValue() % Long.parseLong(value) == 0;
    }

    @Override
    protected Number getArgument(final String value) {
        return Long.parseLong(value);
    }

}
