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

### Java Classic Serialization (don't use this anymore)

1606 bytes, 9.24 µS serialization, 22.43 µS deserialization

```
AC ED 00 05 73 72 00 30 63 68 2E 66 72 6F 73 74 6E 6F 76 61 2E 74 65 73 74 2E 6A 61 63 6B 73 6F 
6E 2E 74 65 73 74 2E 75 74 69 6C 2E 64 6F 6D 61 69 6E 2E 4D 6F 76 69 65 D2 FB 11 A5 A3 78 19 50 
02 00 0A 49 00 04 79 65 61 72 4C 00 06 61 63 74 6F 72 73 74 00 10 4C 6A 61 76 61 2F 75 74 69 6C 
2F 4C 69 73 74 3B 4C 00 0B 61 73 70 65 63 74 52 61 74 69 6F 74 00 38 4C 63 68 2F 66 72 6F 73 74 
6E 6F 76 61 2F 74 65 73 74 2F 6A 61 63 6B 73 6F 6E 2F 74 65 73 74 2F 75 74 69 6C 2F 64 6F 6D 61 
69 6E 2F 41 73 70 65 63 74 52 61 74 69 6F 3B 4C 00 07 63 72 65 61 74 65 64 74 00 13 4C 6A 61 76 
61 2F 74 69 6D 65 2F 49 6E 73 74 61 6E 74 3B 4C 00 08 64 75 72 61 74 69 6F 6E 74 00 14 4C 6A 61 
76 61 2F 74 69 6D 65 2F 44 75 72 61 74 69 6F 6E 3B 4C 00 06 67 65 6E 72 65 73 71 00 7E 00 01 4C 
00 08 6D 65 74 61 64 61 74 61 74 00 35 4C 63 68 2F 66 72 6F 73 74 6E 6F 76 61 2F 74 65 73 74 2F 
6A 61 63 6B 73 6F 6E 2F 74 65 73 74 2F 75 74 69 6C 2F 64 6F 6D 61 69 6E 2F 4D 65 74 61 64 61 74 
61 3B 4C 00 07 72 61 74 69 6E 67 73 74 00 0F 4C 6A 61 76 61 2F 75 74 69 6C 2F 4D 61 70 3B 4C 00 
08 73 79 6E 6F 70 73 69 73 74 00 12 4C 6A 61 76 61 2F 6C 61 6E 67 2F 53 74 72 69 6E 67 3B 4C 00 
05 74 69 74 6C 65 71 00 7E 00 07 78 70 00 00 07 BE 73 72 00 14 6A 61 76 61 2E 75 74 69 6C 2E 4C 
69 6E 6B 65 64 4C 69 73 74 0C 29 53 5D 4A 60 88 22 03 00 00 78 70 77 04 00 00 00 03 73 72 00 30 
63 68 2E 66 72 6F 73 74 6E 6F 76 61 2E 74 65 73 74 2E 6A 61 63 6B 73 6F 6E 2E 74 65 73 74 2E 75 
74 69 6C 2E 64 6F 6D 61 69 6E 2E 41 63 74 6F 72 E1 9B 79 3C 4C 84 69 87 02 00 03 4C 00 09 62 69 
72 74 68 44 61 74 65 74 00 15 4C 6A 61 76 61 2F 74 69 6D 65 2F 4C 6F 63 61 6C 44 61 74 65 3B 4C 
00 09 66 69 72 73 74 4E 61 6D 65 71 00 7E 00 07 4C 00 08 6C 61 73 74 4E 61 6D 65 71 00 7E 00 07 
78 70 73 72 00 0D 6A 61 76 61 2E 74 69 6D 65 2E 53 65 72 95 5D 84 BA 1B 22 48 B2 0C 00 00 78 70 
77 07 03 00 00 07 96 07 0D 78 74 00 08 48 61 72 72 69 73 6F 6E 74 00 04 46 6F 72 64 73 71 00 7E 
00 0B 73 71 00 7E 00 0E 77 07 03 00 00 07 98 01 17 78 74 00 06 52 75 74 67 65 72 74 00 05 48 61 
75 65 72 73 71 00 7E 00 0B 73 71 00 7E 00 0E 77 07 03 00 00 07 A7 0B 14 78 74 00 04 53 65 61 6E 
74 00 05 59 6F 75 6E 67 78 73 72 00 36 63 68 2E 66 72 6F 73 74 6E 6F 76 61 2E 74 65 73 74 2E 6A 
61 63 6B 73 6F 6E 2E 74 65 73 74 2E 75 74 69 6C 2E 64 6F 6D 61 69 6E 2E 41 73 70 65 63 74 52 61 
74 69 6F 48 DC 6B 1C C8 32 B0 85 02 00 02 44 00 06 68 65 69 67 68 74 44 00 05 77 69 64 74 68 78 
70 3F F0 00 00 00 00 00 00 40 03 1E B8 51 EB 85 1F 73 71 00 7E 00 0E 77 0D 02 00 00 00 00 69 B8 
22 24 00 00 00 00 78 73 71 00 7E 00 0E 77 0D 01 00 00 00 00 00 00 1B 6C 00 00 00 00 78 73 71 00 
7E 00 09 77 04 00 00 00 02 7E 72 00 30 63 68 2E 66 72 6F 73 74 6E 6F 76 61 2E 74 65 73 74 2E 6A 
61 63 6B 73 6F 6E 2E 74 65 73 74 2E 75 74 69 6C 2E 64 6F 6D 61 69 6E 2E 47 65 6E 72 65 00 00 00 
00 00 00 00 00 12 00 00 78 72 00 0E 6A 61 76 61 2E 6C 61 6E 67 2E 45 6E 75 6D 00 00 00 00 00 00 
00 00 12 00 00 78 70 74 00 06 53 43 49 5F 46 49 7E 71 00 7E 00 1F 74 00 08 54 48 52 49 4C 4C 45 
52 78 73 72 00 33 63 68 2E 66 72 6F 73 74 6E 6F 76 61 2E 74 65 73 74 2E 6A 61 63 6B 73 6F 6E 2E 
74 65 73 74 2E 75 74 69 6C 2E 64 6F 6D 61 69 6E 2E 4D 65 74 61 64 61 74 61 0D 26 DA 1D 8F 6B A4 
60 02 00 01 4C 00 08 6D 65 74 61 64 61 74 61 71 00 7E 00 06 78 70 73 72 00 11 6A 61 76 61 2E 75 
74 69 6C 2E 54 72 65 65 4D 61 70 0C C1 F6 3E 2D 25 6A E6 03 00 01 4C 00 0A 63 6F 6D 70 61 72 61 
74 6F 72 74 00 16 4C 6A 61 76 61 2F 75 74 69 6C 2F 43 6F 6D 70 61 72 61 74 6F 72 3B 78 70 70 77 
04 00 00 00 03 74 00 08 64 69 72 65 63 74 6F 72 74 00 0C 52 69 64 6C 65 79 20 53 63 6F 74 74 74 
00 0C 72 65 6C 65 61 73 65 2D 64 61 74 65 74 00 0A 31 39 38 32 2D 30 36 2D 32 35 74 00 0A 73 63 
72 65 65 6E 70 6C 61 79 74 00 23 48 61 6D 70 74 6F 6E 20 46 61 6E 63 68 65 72 2C 20 44 61 76 69 
64 20 57 65 62 62 20 50 65 6F 70 6C 65 73 78 73 72 00 11 6A 61 76 61 2E 75 74 69 6C 2E 48 61 73 
68 4D 61 70 05 07 DA C1 C3 16 60 D1 03 00 02 46 00 0A 6C 6F 61 64 46 61 63 74 6F 72 49 00 09 74 
68 72 65 73 68 6F 6C 64 78 70 3F 40 00 00 00 00 00 0C 77 08 00 00 00 10 00 00 00 02 74 00 04 49 
4D 44 42 73 72 00 10 6A 61 76 61 2E 6C 61 6E 67 2E 44 6F 75 62 6C 65 80 B3 C2 4A 29 6B FB 04 02 
00 01 44 00 05 76 61 6C 75 65 78 72 00 10 6A 61 76 61 2E 6C 61 6E 67 2E 4E 75 6D 62 65 72 86 AC 
95 1D 0B 94 E0 8B 02 00 00 78 70 40 20 66 66 66 66 66 66 74 00 0A 4D 65 74 61 63 72 69 74 69 63 
73 71 00 7E 00 33 40 56 40 00 00 00 00 00 78 74 00 85 41 20 62 6C 61 64 65 20 72 75 6E 6E 65 72 
20 6D 75 73 74 20 70 75 72 73 75 65 20 61 6E 64 20 74 65 72 6D 69 6E 61 74 65 20 66 6F 75 72 20 
72 65 70 6C 69 63 61 6E 74 73 0A 20 77 68 6F 20 73 74 6F 6C 65 20 61 20 73 68 69 70 20 69 6E 20 
73 70 61 63 65 20 61 6E 64 20 68 61 76 65 20 72 65 74 75 72 6E 65 64 20 74 6F 20 45 61 72 74 68 
20 74 6F 20 66 69 6E 64 20 74 68 65 69 72 20 63 72 65 61 74 6F 72 2E 74 00 0C 42 6C 61 64 65 20 
52 75 6E 6E 65 72 
```

## Build

### Gradle:

Build with gradle (default: _clean build_):

    ./gradlew

### Maven:

Build with Maven (default: _clean install_):

    mvn
    
    