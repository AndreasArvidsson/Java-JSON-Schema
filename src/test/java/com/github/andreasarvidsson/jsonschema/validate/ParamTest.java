package com.github.andreasarvidsson.jsonschema.validate;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParamTest {

    private final JsonSchemaValidator validator = new JsonSchemaValidator();


    @Test
    public void testParamOk() {
        final ParamClass instance = new ParamClass();
        instance.value1 = new MyDataClass<>();
        instance.value1.value = "value1";
        instance.value2 = new MyDataClass<>();
        instance.value2.value = 2;
        instance.value3 = new ArrayList<>();
        instance.value3.add(new MyDataClass<>());
        instance.value3.get(0).value = 2.5;
        final ValidationReport report = validator.validate(instance);
        Assertions.assertTrue(report.isSuccess(), report.toString());
    }

    class MyDataClass<T> {

        public T value;
    }

    class ParamClass {

        public MyDataClass<String> value1;
        public MyDataClass<Integer> value2;
        public List<MyDataClass<Double>> value3;

    }

}
