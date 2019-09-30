package com.github.andreasarvidsson.jsonschemaform;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 * @author Andreas Arvidsson
 */
public enum SimpleType {

    //INTEGER
    BYTE_PRIMITIVE(byte.class, JsonType.INTEGER, Byte.MIN_VALUE, Byte.MAX_VALUE),
    BYTE_CLASS(Byte.class, JsonType.INTEGER, Byte.MIN_VALUE, Byte.MAX_VALUE),
    SHORT_PRIMITIVE(short.class, JsonType.INTEGER, Short.MIN_VALUE, Short.MAX_VALUE),
    SHORT_CLASS(Short.class, JsonType.INTEGER, Short.MIN_VALUE, Short.MAX_VALUE),
    INTEGER_PRIMITIVE(int.class, JsonType.INTEGER, Integer.MIN_VALUE, Integer.MAX_VALUE),
    INTEGER_CLASS(Integer.class, JsonType.INTEGER, Integer.MIN_VALUE, Integer.MAX_VALUE),
    LONG_PRIMITIVE(long.class, JsonType.INTEGER, Long.MIN_VALUE, Long.MAX_VALUE),
    LONG_CLASS(Long.class, JsonType.INTEGER, Long.MIN_VALUE, Long.MAX_VALUE),
    BIGINTEGER(BigInteger.class, JsonType.INTEGER),
    //NUMBERS
    FLOAT_PRIMITIVE(float.class, JsonType.NUMBER),
    FLOAT_CLASS(Float.class, JsonType.NUMBER),
    DOUBLE_PRIMITIVE(double.class, JsonType.NUMBER),
    DOUBLE_CLASS(Double.class, JsonType.NUMBER),
    BIGDECIMAL(BigDecimal.class, JsonType.NUMBER),
    //BOOLEAN
    BOOLEAN_PRIMITIVE(boolean.class, JsonType.BOOLEAN),
    BOOLEAN_CLASS(Boolean.class, JsonType.BOOLEAN),
    //STRING
    CHAR_PRIMITIVE(char.class, JsonType.STRING),
    CHAR_CLASS(Character.class, JsonType.STRING),
    CHARSEQUENCE(CharSequence.class, JsonType.STRING),
    STRING(String.class, JsonType.STRING);
    //    UUID(UUID.class, "string"),
    //    DATE(Date.class, "string"),
    //    ZONEDDATETIME(ZonedDateTime.class, "string"),
    //    LOCALDATE(LocalDate.class, "string"),
    //    LOCALTIME(LocalTime.class, "string"),
    //    LOCALDATETIME(LocalDateTime.class, "string"),
    //    INSTANT(Instant.class, "string"),
    //    ZONE_ID(ZoneId.class, "string"),
    //    OFFSETDATETIME(OffsetDateTime.class, "string");

    private static final Map<Class<?>, SimpleType> MAP = new IdentityHashMap();

    static {
        for (final SimpleType simpleType : values()) {
            MAP.put(simpleType.type, simpleType);
        }
    }

    public static boolean contains(final Class type) {
        return MAP.containsKey(type);
    }

    public static SimpleType get(final Class type) {
        return MAP.get(type);
    }

    public static boolean isInt(final Class type) {
        final SimpleType st = MAP.get(type);
        return st != null && st.getJsonType() == JsonType.INTEGER;
    }

    public static boolean isNumber(final Class type) {
        final SimpleType st = MAP.get(type);
        return st != null && (st.getJsonType() == JsonType.INTEGER || st.getJsonType() == JsonType.NUMBER);
    }

    public static boolean isBool(final Class type) {
        final SimpleType st = MAP.get(type);
        return st != null && st.getJsonType() == JsonType.BOOLEAN;
    }

    public static boolean isString(final Class type) {
        final SimpleType st = MAP.get(type);
        return st != null && st.getJsonType() == JsonType.STRING;
    }

    final Class type;
    final JsonType jsonType;
    final Number min, max;

    private SimpleType(final Class type, final JsonType jsonType) {
        this(type, jsonType, null, null);
    }

    private SimpleType(final Class type, final JsonType jsonType, final Number min, final Number max) {
        this.type = type;
        this.jsonType = jsonType;
        this.min = min;
        this.max = max;
    }

    public Class getType() {
        return type;
    }

    public JsonType getJsonType() {
        return jsonType;
    }

    public boolean hasMinMax() {
        return min != null;
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

}
