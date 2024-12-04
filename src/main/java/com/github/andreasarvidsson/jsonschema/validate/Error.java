package com.github.andreasarvidsson.jsonschema.validate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.JsonSchema.Combining;
import com.github.andreasarvidsson.jsonschema.JsonSchemaField;
import com.github.andreasarvidsson.jsonschema.JsonSchemaUtil;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class Error {

    public final String message;
    public final String property;
    public final String name;
    public final Object argument;
    private final JsonSchema schema;
    public final Object instance;

    public static Error constant(final String property, final JsonSchema jsonSchema, final Object argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.CONST.toString(),
                argument,
                String.format("Does not exactly match expected constant: %s", jsonSchema.constant()),
                jsonSchema,
                instance);
    }

    public static Error multipleOf(final String property, final JsonSchema jsonSchema, final Number argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MULTIPLE_OF.toString(),
                argument,
                String.format("Is not a multiple of (divisible by) %s", jsonSchema.multipleOf()),
                jsonSchema,
                instance);
    }

    public static Error minimum(final String property, final JsonSchema jsonSchema, final Number argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MINIMUM.toString(),
                argument,
                String.format("Must have a minimum value of %s", jsonSchema.minimum()),
                jsonSchema,
                instance);
    }

    public static Error maximum(final String property, final JsonSchema jsonSchema, final Number argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MAXIMUM.toString(),
                argument,
                String.format("Must have a maximum value of %s", jsonSchema.maximum()),
                jsonSchema,
                instance);
    }

    public static Error exclusiveMinimum(final String property, final JsonSchema jsonSchema, final Number argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.EXCLUSIVE_MINIMUM.toString(),
                argument,
                String.format("Must have an exclusive minimum value of %s", jsonSchema.exclusiveMinimum()),
                jsonSchema,
                instance);
    }

    public static Error exclusiveMaximum(final String property, final JsonSchema jsonSchema, final Number argument,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.EXCLUSIVE_MAXIMUM.toString(),
                argument,
                String.format("Must have an exclusive minimum value of %s", jsonSchema.exclusiveMaximum()),
                jsonSchema,
                instance);
    }

    public static Error minLength(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MIN_LENGTH.toString(),
                jsonSchema.minLength(),
                String.format("Does not meet minimum length of %d", jsonSchema.minLength()),
                jsonSchema,
                instance);
    }

    public static Error maxLength(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MAX_LENGTH.toString(),
                jsonSchema.maxLength(),
                String.format("Does not meet maximum length of %d", jsonSchema.maxLength()),
                jsonSchema,
                instance);
    }

    public static Error pattern(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.PATTERN.toString(),
                jsonSchema.pattern(),
                String.format("Does not match pattern '%s'", jsonSchema.pattern()),
                jsonSchema,
                instance.toString());
    }

    public static Error minItems(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MIN_ITEMS.toString(),
                jsonSchema.minItems(),
                String.format("Does not meet minimum length of %d", jsonSchema.minItems()),
                jsonSchema,
                instance);
    }

    public static Error maxItems(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MAX_ITEMS.toString(),
                jsonSchema.maxItems(),
                String.format("Does not meet maximum length of %d", jsonSchema.maxItems()),
                jsonSchema,
                instance);
    }

    public static Error minProperties(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MIN_PROPERTIES.toString(),
                jsonSchema.minProperties(),
                String.format("Does not meet minimum length of %d", jsonSchema.minProperties()),
                jsonSchema,
                instance);
    }

    public static Error maxProperties(final String property, final JsonSchema jsonSchema, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.MAX_PROPERTIES.toString(),
                jsonSchema.maxProperties(),
                String.format("Does not meet maximum length of %d", jsonSchema.maxProperties()),
                jsonSchema,
                instance);
    }

    public static Error requires(final String property, final JsonSchema jsonSchema, final String propertyName,
            final Object instance) {
        return new Error(
                property,
                JsonSchemaField.Disabled.REQUIRED.toString(),
                propertyName,
                String.format("Requires property '%s'", propertyName),
                jsonSchema,
                instance);
    }

    public static Error dependencies(final String property, final JsonSchema jsonSchema, final String dependencyName,
            final String propertyPath, final Object instance) {
        return new Error(
                property,
                JsonSchemaField.Disabled.DEPENDENCIES.toString(),
                dependencyName,
                String.format("Property %s not found, required by %s", dependencyName, propertyPath),
                jsonSchema,
                instance);
    }

    public static Error anyOf(final String property, final ValidationSubReport subReport) {
        return new Error(
                property,
                Combining.ANY_OF.toString(),
                subReport,
                String.format("Does not match at least one schema among %d", subReport.nrSchemas));
    }

    public static Error oneOf(final String property, final ValidationSubReport subReport) {
        return new Error(
                property,
                Combining.ONE_OF.toString(),
                subReport,
                String.format("Does not match exactly one schema (matched %d / %d)", subReport.matched,
                        subReport.nrSchemas));
    }

    public static Error allOf(final String property, final ValidationSubReport subReport) {
        return new Error(
                property,
                Combining.ALL_OF.toString(),
                subReport,
                String.format("Does not match all required schemas (matched only %d out of %d)", subReport.matched,
                        subReport.nrSchemas));
    }

    public Error(
            final String property, final String name, final Object argument,
            final String message, final JsonSchema schema, final Object instance) {
        this.property = property;
        this.name = name;
        this.argument = argument;
        this.message = message;
        this.schema = schema;
        this.instance = instance;
    }

    private Error(
            final String property, final String name, final Object argument,
            final String message) {
        this(property, name, argument, message, null, null);
    }

    public Map<String, Object> getSchema() {
        return schema != null ? JsonSchemaUtil.toMap(schema) : null;
    }

}
