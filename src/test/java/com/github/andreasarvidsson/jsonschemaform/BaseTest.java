package com.github.andreasarvidsson.jsonschemaform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Andreas Arvidsson
 */
public class BaseTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchemaGenerator gen = new JsonSchemaGenerator();

    @Test
    public void test3() throws JsonProcessingException {
        JsonNode schema = gen.create(MyClass.class);
        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
//        System.out.println(str);
    }

    @Test
    public void test2() {
        //Assertions.assertEquals(1, 1);
    }

}

class MyClass {

    //Integers
    byte bytePrimitive;
    Byte byteClass;
    short shortPrimitive;
    Short shortClass;
    int intPrimitive;
    Integer intClass;
    long longPrimitive;
    Long longClass;
    BigInteger bigInteger;
    //Numbers
    float floatPrimitive;
    Float floatClass;
    double doublePrimitive;
    Double doubleClass;
    BigDecimal bigDecimal;
    //Bool
    boolean boolPrimitive;
    Boolean boolClass;
    //String   
    char charPrimitive;
    Character charClass;
    String string;
    CharSequence charSequence;

}
