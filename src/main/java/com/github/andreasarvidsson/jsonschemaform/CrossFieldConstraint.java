package com.github.andreasarvidsson.jsonschemaform;

/**
 *
 * @author Andreas Arvidsson
 */
public enum CrossFieldConstraint {
    NONE,
    ANY_OF,
    ONE_OF,
    ALL_OF;

    @Override
    public String toString() {
        switch (this) {
            case ANY_OF:
                return "anyOf";
            case ONE_OF:
                return "oneOf";
            case ALL_OF:
                return "allOf";
            default:
                return null;
        }

    }

}
