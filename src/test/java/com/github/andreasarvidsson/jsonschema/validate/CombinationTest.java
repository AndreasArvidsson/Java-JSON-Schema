package com.github.andreasarvidsson.jsonschema.validate;

import com.github.andreasarvidsson.jsonschema.JsonSchema;
import com.github.andreasarvidsson.jsonschema.util.AssertError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class CombinationTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();

    @Test
    public void testAnyOfOk() {
        final AnyOfClass instance = new AnyOfClass();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testAnyOfFail() {
        final AnyOfClass instance = new AnyOfClass();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ANY_OF.toString(),
                "Does not match at least one schema among 2"
        );
    }

    @Test
    public void testOneOfOk() {
        final OneOfClass instance = new OneOfClass();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testOneOfFail0() {
        final OneOfClass instance = new OneOfClass();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)"
        );
    }

    @Test
    public void testOneOfFail2() {
        final OneOfClass instance = new OneOfClass();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 2 / 2)"
        );
    }

    @Test
    public void testAllOfOk() {
        final AllOfClass instance = new AllOfClass();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testAllOfFail0() {
        final AllOfClass instance = new AllOfClass();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 0 out of 2)"
        );
    }

    @Test
    public void testAllOfFail1() {
        final AllOfClass instance = new AllOfClass();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 1 out of 2)"
        );
    }

    @Test
    public void testDependenciesOk() {
        final DependenciesClass instance = new DependenciesClass();
        instance.value3 = 5;
        instance.value4 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testDependenciesFail() {
        final DependenciesClass instance = new DependenciesClass();
        instance.value3 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 1 out of 2)"
        );
    }

    @Test
    public void testMinimumOk() {
        final MinimumCLass instance = new MinimumCLass();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinimumFail0() {
        final MinimumCLass instance = new MinimumCLass();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)"
        );
    }

    @Test
    public void testMinimumFail2() {
        final MinimumCLass instance = new MinimumCLass();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 2 / 2)"
        );
    }

    class AnyOfClass {

        @JsonSchema(
                combining = JsonSchema.Combining.ANY_OF,
                required = true
        )
        public Integer value1;

        @JsonSchema(
                combining = JsonSchema.Combining.ANY_OF,
                required = true
        )
        public Integer value2;

    }

    class OneOfClass {

        @JsonSchema(
                combining = JsonSchema.Combining.ONE_OF,
                required = true
        )
        public Integer value1;

        @JsonSchema(
                combining = JsonSchema.Combining.ONE_OF,
                required = true
        )
        public Integer value2;

    }

    class AllOfClass {

        @JsonSchema(
                combining = JsonSchema.Combining.ALL_OF,
                required = true
        )
        public Integer value1;

        @JsonSchema(
                combining = JsonSchema.Combining.ALL_OF,
                required = true
        )
        public Integer value2;

    }

    class DependenciesClass {

        @JsonSchema(
                combining = JsonSchema.Combining.ALL_OF,
                dependencies = {"value3"}
        )
        public int value1;

        @JsonSchema(
                combining = JsonSchema.Combining.ALL_OF,
                dependencies = {"value4"}
        )
        public int value2;

        public Integer value3;
        public Integer value4;

    }

    class MinimumCLass {

        @JsonSchema(
                combining = JsonSchema.Combining.ONE_OF,
                minimum = "2"
        )
        public int value1;

        @JsonSchema(
                combining = JsonSchema.Combining.ONE_OF,
                minimum = "4"
        )
        public int value2;

    }

}
