# JSON/XML/YAML Test

Testing Jackson JSON/XML/YAML object mapping

## Features

- shows how to properly configure the Jackson `ObjectMapper` (with Java 8 Date/Time API support)
- demonstrates how to serialize/deserialize JSON, XML and YAML with Jackson
- **bean** serialization &rarr; `Movie`, `Actor`
- custom serialization for **enums** using arbitrary string identifiers, ignoring case &rarr; `Genre`
- custom serialization for **domain object** &rarr; `Metadata` with `MetadataConverter.Serializer`
  and `MetadataConverter.Deserializer`
- custom serialization for **immutable value objects** with string constructor / `toString()` &rarr; `AspectRatio`
- custom serialization for `java.time.Duration` in a user-friendly format (e.g. `"1w 2d 3h 4m 5s"`)
- function-based custom serialization using `FunctionalSerializer` / `FunctionalDeserializer`
- *NEW*: **JSON schema** and validation example
- *NEW*: **Protobuf** example

## Examples

Serialized examples (`Movie` with `Actor`, `Genre`, `AspectRatio` and `Metadata`):

### JSON

887 bytes, 3.24 µS serialization, 4.48 µS deserialization

```json
{
  "title" : "Blade Runner",
  "year" : 1982,
  "genres" : [ "Sci-Fi", "Thriller" ],
  "aspect-ratio" : "2.39:1",
  "created" : "2026-03-16T14:45:28Z",
  "ratings" : {
    "IMDB" : 8.2,
    "Metacritic" : 89.0
  },
  "synopsis" : "A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.",
  "actors" : [ {
    "firstName" : "Harrison",
    "lastName" : "Ford",
    "dateOfBirth" : "1942-07-13",
    "age" : 83
  }, {
    "firstName" : "Rutger",
    "lastName" : "Hauer",
    "dateOfBirth" : "1944-01-23",
    "age" : 82
  }, {
    "firstName" : "Sean",
    "lastName" : "Young",
    "dateOfBirth" : "1959-11-20",
    "age" : 66
  } ],
  "duration" : "1h 57m",
  "metadata" : {
    "director" : "Ridley Scott",
    "release-date" : "1982-06-25",
    "screenplay" : "Hampton Fancher, David Webb Peoples"
  }
}
```

### XML

997 bytes, 6.05 µS serialization, 11.60 µS deserialization

```xml
<movie created="2026-03-16T14:45:26Z">
  <ratings IMDB="8.2" Metacritic="89.0"/>
  <title>Blade Runner</title>
  <year>1982</year>
  <genre>Sci-Fi</genre>
  <genre>Thriller</genre>
  <aspectRatio>2.39:1</aspectRatio>
  <synopsis>A blade runner must pursue and terminate four replicants
    who stole a ship in space and have returned to Earth to find their creator.</synopsis>
  <actor age="83">
    <firstName>Harrison</firstName>
    <lastName>Ford</lastName>
    <dateOfBirth>1942-07-13</dateOfBirth>
  </actor>
  <actor age="82">
    <firstName>Rutger</firstName>
    <lastName>Hauer</lastName>
    <dateOfBirth>1944-01-23</dateOfBirth>
  </actor>
  <actor age="66">
    <firstName>Sean</firstName>
    <lastName>Young</lastName>
    <dateOfBirth>1959-11-20</dateOfBirth>
  </actor>
  <duration>1h 57m</duration>
  <metadata>
    <director>Ridley Scott</director>
    <release-date>1982-06-25</release-date>
    <screenplay>Hampton Fancher, David Webb Peoples</screenplay>
  </metadata>
</movie>
```

### YAML

665 bytes, 15.36 µS serialization, 30.92 µS deserialization

```yaml
---
title: Blade Runner
year: 1982
genres:
  - Sci-Fi
  - Thriller
aspect-ratio: 2.39:1
created: 2026-03-16T14:45:29Z
ratings:
  IMDB: 8.2
  Metacritic: 89.0
synopsis: |-
  A blade runner must pursue and terminate four replicants
   who stole a ship in space and have returned to Earth to find their creator.
actors:
  - firstName: Harrison
    lastName: Ford
    dateOfBirth: 1942-07-13
    age: 83
  - firstName: Rutger
    lastName: Hauer
    dateOfBirth: 1944-01-23
    age: 82
  - firstName: Sean
    lastName: Young
    dateOfBirth: 1959-11-20
    age: 66
duration: 1h 57m
metadata:
  director: Ridley Scott
  release-date: 1982-06-25
  screenplay: "Hampton Fancher, David Webb Peoples"
```

### Java Properties

726 bytes, 7.16 µS serialization, 11.80 µS deserialization

```properties
title=Blade Runner
year=1982
genres.1=Sci-Fi
genres.2=Thriller
aspect-ratio=2.39:1
created=2026-03-16T14:45:24Z
ratings.IMDB=8.2
ratings.Metacritic=89.0
synopsis=A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.
actors.1.firstName=Harrison
actors.1.lastName=Ford
actors.1.dateOfBirth=1942-07-13
actors.1.age=83
actors.2.firstName=Rutger
actors.2.lastName=Hauer
actors.2.dateOfBirth=1944-01-23
actors.2.age=82
actors.3.firstName=Sean
actors.3.lastName=Young
actors.3.dateOfBirth=1959-11-20
actors.3.age=66
duration=1h 57m
metadata.director=Ridley Scott
metadata.release-date=1982-06-25
metadata.screenplay=Hampton Fancher, David Webb Peoples
```

### CBOR (https://tools.ietf.org/html/rfc7049)

607 bytes, 2.20 µS serialization, 3.96 µS deserialization

```
BF 65 74 69 74 6C 65 6C 42 6C 61 64 65 20 52 75 6E 6E 65 72 64 79 65 61 72 19 07 BE 66 67 65 6E 
72 65 73 82 66 53 63 69 2D 46 69 68 54 68 72 69 6C 6C 65 72 6C 61 73 70 65 63 74 2D 72 61 74 69 
6F 66 32 2E 33 39 3A 31 67 63 72 65 61 74 65 64 74 32 30 32 36 2D 30 33 2D 31 36 54 31 34 3A 35 
37 3A 32 32 5A 67 72 61 74 69 6E 67 73 BF 64 49 4D 44 42 FB 40 20 66 66 66 66 66 66 6A 4D 65 74 
61 63 72 69 74 69 63 FB 40 56 40 00 00 00 00 00 FF 68 73 79 6E 6F 70 73 69 73 78 85 41 20 62 6C 
61 64 65 20 72 75 6E 6E 65 72 20 6D 75 73 74 20 70 75 72 73 75 65 20 61 6E 64 20 74 65 72 6D 69 
6E 61 74 65 20 66 6F 75 72 20 72 65 70 6C 69 63 61 6E 74 73 0A 20 77 68 6F 20 73 74 6F 6C 65 20 
61 20 73 68 69 70 20 69 6E 20 73 70 61 63 65 20 61 6E 64 20 68 61 76 65 20 72 65 74 75 72 6E 65 
64 20 74 6F 20 45 61 72 74 68 20 74 6F 20 66 69 6E 64 20 74 68 65 69 72 20 63 72 65 61 74 6F 72 
2E 66 61 63 74 6F 72 73 83 BF 69 66 69 72 73 74 4E 61 6D 65 68 48 61 72 72 69 73 6F 6E 68 6C 61 
73 74 4E 61 6D 65 64 46 6F 72 64 6B 64 61 74 65 4F 66 42 69 72 74 68 6A 31 39 34 32 2D 30 37 2D 
31 33 63 61 67 65 18 53 FF BF 69 66 69 72 73 74 4E 61 6D 65 66 52 75 74 67 65 72 68 6C 61 73 74 
4E 61 6D 65 65 48 61 75 65 72 6B 64 61 74 65 4F 66 42 69 72 74 68 6A 31 39 34 34 2D 30 31 2D 32 
33 63 61 67 65 18 52 FF BF 69 66 69 72 73 74 4E 61 6D 65 64 53 65 61 6E 68 6C 61 73 74 4E 61 6D 
65 65 59 6F 75 6E 67 6B 64 61 74 65 4F 66 42 69 72 74 68 6A 31 39 35 39 2D 31 31 2D 32 30 63 61 
67 65 18 42 FF 68 64 75 72 61 74 69 6F 6E 66 31 68 20 35 37 6D 68 6D 65 74 61 64 61 74 61 BF 68 
64 69 72 65 63 74 6F 72 6C 52 69 64 6C 65 79 20 53 63 6F 74 74 6C 72 65 6C 65 61 73 65 2D 64 61 
74 65 6A 31 39 38 32 2D 30 36 2D 32 35 6A 73 63 72 65 65 6E 70 6C 61 79 78 23 48 61 6D 70 74 6F 
6E 20 46 61 6E 63 68 65 72 2C 20 44 61 76 69 64 20 57 65 62 62 20 50 65 6F 70 6C 65 73 FF FF 
```

### Protobuf (https://protobuf.dev/)

414 bytes, 1.62 µS serialization, 1.82 µS deserialization

```
0A 0D 31 37 37 33 36 37 33 30 34 35 30 30 30 12 0C 42 6C 61 64 65 20 52 75 6E 6E 65 72 18 BE 0F 
22 02 00 01 2A 0F 0A 04 49 4D 44 42 11 66 66 66 66 66 66 20 40 2A 15 0A 0A 4D 65 74 61 63 72 69 
74 69 63 11 00 00 00 00 00 40 56 40 32 85 01 41 20 62 6C 61 64 65 20 72 75 6E 6E 65 72 20 6D 75 
73 74 20 70 75 72 73 75 65 20 61 6E 64 20 74 65 72 6D 69 6E 61 74 65 20 66 6F 75 72 20 72 65 70 
6C 69 63 61 6E 74 73 0A 20 77 68 6F 20 73 74 6F 6C 65 20 61 20 73 68 69 70 20 69 6E 20 73 70 61 
63 65 20 61 6E 64 20 68 61 76 65 20 72 65 74 75 72 6E 65 64 20 74 6F 20 45 61 72 74 68 20 74 6F 
20 66 69 6E 64 20 74 68 65 69 72 20 63 72 65 61 74 6F 72 2E 3A 1C 0A 08 48 61 72 72 69 73 6F 6E 
12 04 46 6F 72 64 1A 0A 31 39 34 32 2D 30 37 2D 31 33 3A 1B 0A 06 52 75 74 67 65 72 12 05 48 61 
75 65 72 1A 0A 31 39 34 34 2D 30 31 2D 32 33 3A 19 0A 04 53 65 61 6E 12 05 59 6F 75 6E 67 1A 0A 
31 39 35 39 2D 31 31 2D 32 30 42 06 32 2E 33 39 3A 31 48 EC 36 52 18 0A 08 64 69 72 65 63 74 6F 
72 12 0C 52 69 64 6C 65 79 20 53 63 6F 74 74 52 1A 0A 0C 72 65 6C 65 61 73 65 2D 64 61 74 65 12 
0A 31 39 38 32 2D 30 36 2D 32 35 52 31 0A 0A 73 63 72 65 65 6E 70 6C 61 79 12 23 48 61 6D 70 74 
6F 6E 20 46 61 6E 63 68 65 72 2C 20 44 61 76 69 64 20 57 65 62 62 20 50 65 6F 70 6C 65 73 
```

## Build

### Gradle:

Build with gradle (default: _clean build_):

    ./gradlew

### Maven:

Build with Maven (default: _clean install_):

    mvn
    
    