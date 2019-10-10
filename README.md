# Java JSON Schema

Adds JSON schema support to domain objects.    
* Enables both schema generation and instance validation.
* Supports most of the Draft-06 specification.

# JsonSchema anotations
```java
@JsonSchema(
        title = "My example class",
        description = "This is an example of a class using JsonSchema"
)
class MyClass {

    @JsonSchema(
            title = "A string",
            description = "This is a string",
            minLength = 1,
            maxLength = 100,
            pattern = "\\s",
            format = "myFormat",
            required = true
    )
    public String str;

    @JsonSchema(
            minimum = "5",
            maximum = "18",
            multipleOf = "2",
            constant = "55",
            dependencies = { "number" }
    )
    public int integer;

    @JsonSchema(
            exclusiveMinimum = "0.0",
            exclusiveMaximum = "1.0"
    )
    public double number;

    @JsonSchema(
            minItems = 0,
            maxItems = 20,
            combining = JsonSchema.Combining.ONE_OF
    )
    public List<Integer> intList;

    @JsonSchema(
            minProperties = 0,
            maxProperties = 20,
            combining = JsonSchema.Combining.ONE_OF
    )
    public Map<String, Boolean> boolMap;
    
}
```
```json
{
    "$schema": "http://json-schema.org/draft-06/schema#",
    "type": "object",
    "title": "My example class",
    "description": "This is an example of a class using JsonSchema",
    "additionalProperties": false,
    "required": [ "str", "integer", "number" ],
    "dependencies": {
        "integer": [ "number" ]
    },
    "properties": {
        "str": {
            "type": "string",
            "title": "A string",
            "description": "This is a string",
            "minLength": 1,
            "maxLength": 100,
            "pattern": "\\s",
            "format": "myFormat"
        },
        "integer": {
            "type": "integer",
            "minimum": 5,
            "maximum": 18,
            "multipleOf": 2,
            "const": 55
        },
        "number": {
            "type": "number",
            "exclusiveMinimum": 0.0,
            "exclusiveMaximum": 1.0
        },
        "intList": {
            "type": "array",
            "items": {
                "type": "integer",
                "minimum": -2147483648,
                "maximum": 2147483647
            }
        },
        "boolMap": {
            "type": "object",
            "patternProperties": {
                "^.*$": {
                    "type": "boolean"
                }
            }
        }
    },
    "oneOf": [
        {
            "properties": {
                "intList": {
                    "minItems": 0,
                    "maxItems": 20
                }
            }
        },
        {
            "properties": {
                "intMap": {
                    "minProperties": 0,
                    "maxProperties": 20
                }
            }
        }
    ]
}
```

# JSON schema generation
Generate a json schema for a specific class/type.
* Since Java classes have fixed members `additionalProperties = false` is added by default.
    - Se Jackson anotations @JsonAnyGetter / @JsonAnySetter to enable dynamic properties. 
* Primitive types like int, double and boolean will always be required since they can't be null.
    - Use class types Integer, Double and Boolean to get nullable and non-required properties.
* Numbers will automatically get data type ranges(minimum, maximum) added if not specified othervise.
    - int will by default have `minimum = Integer.MIN_VALUE, maximum = Integer.MAX_VALUE`.
* Sets gets `uniqueItems = true` while other collections doesn't.
* Characters/chars is type `string` but with `minLength = 1, maxLength = 1`.

```java
JsonSchemaGenerator gen = new JsonSchemaGenerator();
JsonNode schema = gen.generate(MyClass.class);
```

## Generator options

```java
JsonSchemaGenerator gen = new JsonSchemaGenerator()
        //Hide the root field $schema
        .hideSchemaField()
        //Set a custom $schema field
        .setSchemaField(new URI("http://www.example.com/schema"))
        //Disable the addition of auto range(minimum, maximum) values.
        .disableAutoRangeNumbers();
```

## Custom generators
Register custom generators
```java
class CustomGenerator implements Generator  { ... }

JsonSchemaGenerator gen = new JsonSchemaGenerator()
        .addCustomGenerator(MyClass.class, new CustomGenerator());
```

# JSON schema validation
Validate an existing instance using its class anotations.
```java
JsonSchemaValidator validator = new JsonSchemaValidator();
MyClass instance = new MyClass();
ValidationReport report = validator.validate(instance);
Assertions.assertTrue(report.isSuccess(), report.toString());
```

## Custom validators
Register custom validators.
```java
class CustomValidator implements Validator  { ... }

JsonSchemaValidator validator = new JsonSchemaValidator()
        .addCustomValidator(MyClass.class, new CustomValidator());
```

## JsonSchemaEnum interface
If a class implements the `JsonSchemaEnum` interface the returned title and description will be added to the generated schema.    
* Return null to not generate specific title and/or description.
```java
public enum MyClass implements JsonSchemaEnum {
    A, B, C;

    @JsonValue
    public Object getVal() {
        return toString();
    }

    @Override
    public String getTitle() {
        return toString().toLowerCase();
    }

    @Override
    public String getDescription() {
        return toString() + "_desc";
    }
}
```
```json
{
    "oneOf": [
        {
            "const": "A",
            "title": "a",
            "description": "A_desc"
        },
        {
            "const": "B",
            "title": "b",
            "description": "B_desc"
        },
        {
            "const": "C",
            "title": "c",
            "description": "C_desc"
        }
    ]
}
```

# Jackson anotation support

* @JsonIgnore    
If a field is anotated with `@JsonIgnore` it will not be generated or validated.

* @JsonPropertyOrder    
If a class is anotated with `@JsonPropertyOrder` the order in the generated properties field will be matching.

* @JsonAnyGetter / @JsonAnySetter    
If a class have `@JsonAnyGetter` and `@JsonAnySetter` methods `additionalProperties = true` will be added in generation.

* @JsonValue    
If a method is anotated with `@JsonValue` its return type vill be used instead of the class for both generation and validation.

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