package com.github.andreasarvidsson.jsonschemaform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author andreas.arvidsson@redpill-linpro.com
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSchema {
    
    //General
    String title() default "";
    String description() default "";
    
    //Object
    long minProperties() default -1;
    long maxProperties() default -1;
    boolean required() default false;    
    //dependencies: Map<String, String[]>
        
    //Array
    long minItems() default -1;
    long maxItems() default -1;
    
    //String
    long minLength() default -1;
    long maxLength() default -1;
    String pattern() default "";
    String format() default "";
    
    //Number / integer
    long minimum() default Long.MIN_VALUE;
    long maximum() default Long.MAX_VALUE;
    long exclusiveMaximum() default Long.MIN_VALUE;
    long exclusiveMinimum() default Long.MAX_VALUE;
    long multipleOf() default 0;
    
    CrossFieldConstraint crossFieldConstraint() default CrossFieldConstraint.NONE;
    int crossFieldConstraintGroup() default -1;

}
