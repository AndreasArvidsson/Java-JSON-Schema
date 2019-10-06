package com.github.andreasarvidsson.jsonschema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Andreas Arvidsson
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JsonSchemas.class)
public @interface JsonSchema {
    
    //General
    String title() default "";
    String description() default "";
    
    //Object
    long minProperties() default Long.MIN_VALUE;
    long maxProperties() default Long.MIN_VALUE;
    boolean required() default false;    
    String[] dependencies() default {};
        
    //Array
    long minItems() default Long.MIN_VALUE;
    long maxItems() default Long.MIN_VALUE;
    
    //String
    long minLength() default Long.MIN_VALUE;
    long maxLength() default Long.MIN_VALUE;
    String pattern() default "";
    String format() default "";
    
    //Number / integer
    long minimum() default Long.MIN_VALUE;
    long maximum() default Long.MAX_VALUE;
    long exclusiveMaximum() default Long.MIN_VALUE;
    long exclusiveMinimum() default Long.MAX_VALUE;
    long multipleOf() default 0;
    
    Combining combining() default Combining.NONE;
    long combiningGroup() default Long.MIN_VALUE;

    public static enum Combining {
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

}
