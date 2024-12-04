package com.github.andreasarvidsson.jsonschema;

import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Andreas Arvidsson
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JsonSchemas.class)
public @interface JsonSchema {

    // General
    String title() default "";

    String description() default "";

    String constant() default "";

    // Object
    long minProperties() default Long.MIN_VALUE;

    long maxProperties() default Long.MIN_VALUE;

    boolean required() default false;

    String[] dependencies() default {};

    // Array
    long minItems() default Long.MIN_VALUE;

    long maxItems() default Long.MIN_VALUE;

    // String
    long minLength() default Long.MIN_VALUE;

    long maxLength() default Long.MIN_VALUE;

    String pattern() default "";

    String format() default "";

    // Number / integer
    String minimum() default "";

    String maximum() default "";

    String exclusiveMaximum() default "";

    String exclusiveMinimum() default "";

    String multipleOf() default "";

    Combining combining() default Combining.NONE;

    long combiningGroup() default Long.MIN_VALUE;

    public static enum Combining {
        NONE,
        ANY_OF,
        ONE_OF,
        ALL_OF;

        @Override
        @JsonValue
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

}
