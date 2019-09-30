package com.github.andreasarvidsson.jsonschemaform.parsers;

import com.github.andreasarvidsson.jsonschemaform.JsonSchemaField;
import com.github.andreasarvidsson.jsonschemaform.JsonType;
import java.util.Arrays;

/**
 *
 * @author Andreas Arvidsson
 */
public class ParserInteger extends ParserBase {

    private final boolean autoRangeNumbers;

    public ParserInteger(final boolean autoRangeNumbers) {
        super(JsonType.INTEGER, Arrays.asList(
                JsonSchemaField.TITLE,
                JsonSchemaField.DESCRIPTION,
                JsonSchemaField.MINIMUM,
                JsonSchemaField.MAXIMUM,
                JsonSchemaField.EXCLUSIVE_MINIMUM,
                JsonSchemaField.EXCLUSIVE_MAXIMUM,
                JsonSchemaField.MULTIPLE_OF
        ));
        this.autoRangeNumbers = autoRangeNumbers;
    }

}

//    BYTE_PRIMITIVE(byte.class, JsonType.INTEGER, Byte.MIN_VALUE, Byte.MAX_VALUE),
//    BYTE_CLASS(Byte.class, JsonType.INTEGER, Byte.MIN_VALUE, Byte.MAX_VALUE),
//    SHORT_PRIMITIVE(short.class, JsonType.INTEGER, Short.MIN_VALUE, Short.MAX_VALUE),
//    SHORT_CLASS(Short.class, JsonType.INTEGER, Short.MIN_VALUE, Short.MAX_VALUE),
//    INTEGER_PRIMITIVE(int.class, JsonType.INTEGER, Integer.MIN_VALUE, Integer.MAX_VALUE),
//    INTEGER_CLASS(Integer.class, JsonType.INTEGER, Integer.MIN_VALUE, Integer.MAX_VALUE),
//    LONG_PRIMITIVE(long.class, JsonType.INTEGER, Long.MIN_VALUE, Long.MAX_VALUE),
//    LONG_CLASS(Long.class, JsonType.INTEGER, Long.MIN_VALUE, Long.MAX_VALUE),
//    BIGINTEGER(BigInteger.class, JsonType.INTEGER),
