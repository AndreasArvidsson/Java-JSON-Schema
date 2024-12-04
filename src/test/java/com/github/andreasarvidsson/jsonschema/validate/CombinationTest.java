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
        final CombiningAnyOf instance = new CombiningAnyOf();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testAnyOfFail() {
        final CombiningAnyOf instance = new CombiningAnyOf();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ANY_OF.toString(),
                "Does not match at least one schema among 2");
    }

    @Test
    public void testOneOfOk() {
        final CombiningOneOf instance = new CombiningOneOf();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testOneOfFail0() {
        final CombiningOneOf instance = new CombiningOneOf();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)");
    }

    @Test
    public void testOneOfFail2() {
        final CombiningOneOf instance = new CombiningOneOf();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 2 / 2)");
    }

    @Test
    public void testAllOfOk() {
        final CombiningAllOf instance = new CombiningAllOf();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testAllOfFail0() {
        final CombiningAllOf instance = new CombiningAllOf();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 0 out of 2)");
    }

    @Test
    public void testAllOfFail1() {
        final CombiningAllOf instance = new CombiningAllOf();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 1 out of 2)");
    }

    @Test
    public void testDependenciesOk() {
        final CombiningDependencies instance = new CombiningDependencies();
        instance.value3 = 5;
        instance.value4 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testDependenciesFail() {
        final CombiningDependencies instance = new CombiningDependencies();
        instance.value3 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ALL_OF.toString(),
                "Does not match all required schemas (matched only 1 out of 2)");
    }

    @Test
    public void testMinimumOk() {
        final CombiningMinimum instance = new CombiningMinimum();
        instance.value1 = 5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testMinimumFail0() {
        final CombiningMinimum instance = new CombiningMinimum();
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)");
    }

    @Test
    public void testMinimumFail2() {
        final CombiningMinimum instance = new CombiningMinimum();
        instance.value1 = 5;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 2 / 2)");
    }

    @Test
    public void testGroup0OkDigit() {
        final CombiningGroupZero instance = new CombiningGroupZero();
        instance.value1 = "5";
        instance.value2 = "5";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testConstOk() {
        final ConstClass instance = new ConstClass();
        instance.value1 = true;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testConstFail() {
        final ConstClass instance = new ConstClass();
        instance.value1 = true;
        instance.value2 = 5;
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 2 / 2)");
    }

    @Test
    public void testGroup0OkText() {
        final CombiningGroupZero instance = new CombiningGroupZero();
        instance.value1 = "abcde";
        instance.value2 = "ab";
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    @Test
    public void testGroup0Fail1() {
        final CombiningGroupZero instance = new CombiningGroupZero();
        instance.value1 = "abcd";
        instance.value2 = "ab";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)");
    }

    @Test
    public void testGroup0Fail2() {
        final CombiningGroupZero instance = new CombiningGroupZero();
        instance.value1 = "5";
        instance.value2 = "a";
        final ValidationReport report = validator.validate(instance);
        AssertError.assertErrorMessage(
                report,
                report.propertyPath,
                JsonSchema.Combining.ONE_OF.toString(),
                "Does not match exactly one schema (matched 0 / 2)");
    }

    class CombiningAnyOf {

        @JsonSchema(combining = JsonSchema.Combining.ANY_OF, required = true)
        public Integer value1;

        @JsonSchema(combining = JsonSchema.Combining.ANY_OF, required = true)
        public Integer value2;

    }

    class CombiningOneOf {

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, required = true)
        public Integer value1;

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, required = true)
        public Integer value2;

    }

    class CombiningAllOf {

        @JsonSchema(combining = JsonSchema.Combining.ALL_OF, required = true)
        public Integer value1;

        @JsonSchema(combining = JsonSchema.Combining.ALL_OF, required = true)
        public Integer value2;

    }

    class CombiningDependencies {

        @JsonSchema(combining = JsonSchema.Combining.ALL_OF, dependencies = { "value3" })
        public int value1;

        @JsonSchema(combining = JsonSchema.Combining.ALL_OF, dependencies = { "value4" })
        public int value2;

        public Integer value3;
        public Integer value4;

    }

    class CombiningMinimum {

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, minimum = "2")
        public int value1;

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, minimum = "4")
        public int value2;

    }

    class CombiningGroupZero {

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, combiningGroup = 0, pattern = "\\d")
        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, combiningGroup = 0, minLength = 5)
        public String value1;

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, combiningGroup = 0, pattern = "\\d")
        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, combiningGroup = 0, minLength = 2)
        public String value2;

    }

    class ConstClass {

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, constant = "true")
        public boolean value1;

        @JsonSchema(combining = JsonSchema.Combining.ONE_OF, required = true)
        public Integer value2;

    }

}
