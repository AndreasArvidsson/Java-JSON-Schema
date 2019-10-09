package com.github.andreasarvidsson.jsonschema.validate.validators;

/**
 *
 * @author Andreas Arvidsson
 */
public class ValidatorNumber extends ValidatorNumberBase {

    final static double DBL_EPSILON = 2.2204460492503131E-16;
    final static float FLT_EPSILON = 1.19209290E-07F;

    @Override
    protected int compareTo(final Number instance, final String value) {
        //Float
        if (instance instanceof Float) {
            final float val = Float.parseFloat(value);
            if (equals(instance.floatValue(), val)) {
                return 0;
            }
            return Float.compare(instance.floatValue(), val);
        }
        //Double
        final double val = Double.parseDouble(value);
        if (equals(instance.doubleValue(), val)) {
            return 0;
        }
        return Double.compare(instance.doubleValue(), val);
    }

    @Override
    protected boolean multipleOf(final Number instance, final String value) {
        //Float
        if (instance instanceof Float) {
            return equals(instance.floatValue() % Float.parseFloat(value), 0.0f);
        }
        //Double
        return equals(instance.doubleValue() % Double.parseDouble(value), 0.0);
    }

    @Override
    protected Number getArgument(final String value) {
        return Double.parseDouble(value);
    }

    private boolean equals(final float a, final float b) {
        return Math.abs(a - b) < FLT_EPSILON;
    }

    private boolean equals(final double a, final double b) {
        return Math.abs(a - b) < DBL_EPSILON;
    }

}
