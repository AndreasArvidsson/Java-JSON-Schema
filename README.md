# Java JSON Schema

Adds JSON schema support to domain objects.

# Combining schemas (anyOf, oneOf, allOf)

## No combining groups

By default each combining anotation is is its own group. IE an own object in the array.

```java
class CombiningWithoutGroup {

    @JsonSchema(
            combining = Combining.ONE_OF,
            required = true
    )
    String value1;

    @JsonSchema(
            combining = Combining.ONE_OF,
            required = true
    )
    String value2;

    @JsonSchema(
            combining = Combining.ONE_OF,
            required = true
    )
    String value3;

}
```

```json
{
    "type" : "object",
    "properties" : {
        "value1" : {
            "type" : "string"
        },
        "value2" : {
            "type" : "string"
        }
    },
    "oneOf" : [
        {
            "required" : [ "value1" ]
        },
        {
            "required" : [ "value2" ]
        },
        {
            "required" : [ "value3" ]
        }
    ]
}
```

## With combining group

By specifying a combining group above 0 the anotations with the same group is combined.

```java
class CombiningWithGroup {

@JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 1,
        required = true
)
String value1;

@JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 1,
        required = true
)
String value2;

@JsonSchema(
        combining = Combining.ONE_OF,
        required = true
)
String value3;

}
```

```json
{
    "type" : "object",
    "properties" : {
        "value1" : {
            "type" : "string"
        },
        "value2" : {
            "type" : "string"
        },
        "value3" : {
            "type" : "string"
        }
    },
    "oneOf" : [
        {
            "required" : [ "value1", "value2" ]
        },
        {
            "required" : [ "value3" ]
        } 
    ]
}
```

## With combining group and multiple anotations

Using multiple anotations with different combining groups you can combine schemas between different properties.

```java
class CombiningWithGroupAndMultiple {

    @JsonSchema(
            combining = Combining.ONE_OF,
            combiningGroup = 1,
            minLength = 2
    )
    @JsonSchema(
            combining = Combining.ONE_OF,
            combiningGroup = 2,
            pattern = "\\d"
    )
    String value1;

    @JsonSchema(
            combining = Combining.ONE_OF,
            combiningGroup = 1,
            pattern = "\\s"
    )
    @JsonSchema(
            combining = Combining.ONE_OF,
            combiningGroup = 2,
            minLength = 5
    )
    String value2;
}
```

```json
{
    "type" : "object",
    "properties" : {
        "value1" : {
            "type" : "string"
        },
        "value2" : {
            "type" : "string"
        }
    },
    "oneOf" : [
        {
            "properties" : {
                "value1" : {
                  "minLength": 2  
                },
                "value2" : {
                    "pattern": "\\s"
                }
            },
        },
        {
            "properties" : {
                "value1" : {
                    "pattern": "\\d"
                },
                "value2" : {
                    "minLength": 5
                }
            },
        }
    ]
}
```

## Single field

By default if only one field has anotations the combination schema will be applied on the property level.

```java
class CombiningSingleField {

    @JsonSchema(
        combining = Combining.ONE_OF,
        pattern = "\\d"
    )
    @JsonSchema(
        combining = Combining.ONE_OF,
        minLength = 5
    )
    String value1;

    String value2;

}
```

```json
{
    "type" : "object",
    "properties" : {
        "value1" : {
            "type" : "string",
            "oneOf" : [
                {
                   "pattern": "\\d"
                },
                {
                    "minLength" : 5
                } 
            ]
        },
        "value2" : {
            "type" : "string"
        }
    }
}
```

## Combining group 0 - Own property

By using combining group 0 all anotations on that member/field will be at the property level.

```java
class CombiningGroupZero {

    @JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 0,
        pattern = "\\d"
    )
    @JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 0,
        minLength = 5
    )
    String value1;

    @JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 0,
        pattern = "\\s"
    )
    @JsonSchema(
        combining = Combining.ONE_OF,
        combiningGroup = 0,
        minLength = 2
    )
    String value2;

}
```

```json
{
    "type" : "object",
    "properties" : {
        "value1" : {
            "type" : "string",
            "oneOf" : [
                {
                   "pattern": "\\d"
                },
                {
                    "minLength" : 5
                } 
            ]
        },
        "value2" : {
            "type" : "string",
             "oneOf" : [
                {
                   "pattern": "\\s"
                },
                {
                    "minLength" : 2
                } 
            ]
        }
    }
}
```