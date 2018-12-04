#  JSON/XML/YAML Test
Testing Jackson JSON/XML/YAML object mapping

## Features

- shows how to properly configure the Jackson `ObjectMapper` (with Java 8 Date/Time API support)
- demonstrates how to serialize/deserialize JSON, XML and YAML with Jackson
- bean serialization &rarr; `Movie`, `Actor`
- function-based custom serialization using `FunctionalSerializer` / `FunctionalDesrializer`
- custom serialization for enums using arbitrary string identifiers, ignoring case &rarr; `Genre`
- custom serialization for immutable value objects with string constructor / `toString()` &rarr; `AspectRatio`

## Examples

Serialized examples (`Movie` with `Actor`, `Genre` and `AspectRatio`):

### JSON (742 bytes)

    {
      "title" : "Blade Runner",
      "year" : 1982,
      "genres" : [ "Sci-Fi", "Thriller" ],
      "aspect-ratio" : "2.39:1",
      "created" : "2018-12-04T12:17:20Z",
      "ratings" : {
        "IMDB" : 8.2,
        "Metacritic" : 89.0
      },
      "synopsis" : "A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.",
      "actors" : [ {
        "firstName" : "Harrison",
        "lastName" : "Ford",
        "dateOfBirth" : "1942-07-13",
        "age" : 76
      }, {
        "firstName" : "Rutger",
        "lastName" : "Hauer",
        "dateOfBirth" : "1944-01-23",
        "age" : 74
      }, {
        "firstName" : "Sean",
        "lastName" : "Young",
        "dateOfBirth" : "1959-11-20",
        "age" : 59
      } ]
    }

### XML (829 bytes)

    <movie>
      <ratings IMDB="8.2" Metacritic="89.0"/>
      <created>2018-12-04T12:17:21Z</created>
      <title>Blade Runner</title>
      <year>1982</year>
      <genre>Sci-Fi</genre>
      <genre>Thriller</genre>
      <aspectRatio>2.39:1</aspectRatio>
      <synopsis>A blade runner must pursue and terminate four replicants
     who stole a ship in space and have returned to Earth to find their creator.</synopsis>
      <actor age="76">
        <firstName>Harrison</firstName>
        <lastName>Ford</lastName>
        <dateOfBirth>1942-07-13</dateOfBirth>
      </actor>
      <actor age="74">
        <firstName>Rutger</firstName>
        <lastName>Hauer</lastName>
        <dateOfBirth>1944-01-23</dateOfBirth>
      </actor>
      <actor age="59">
        <firstName>Sean</firstName>
        <lastName>Young</lastName>
        <dateOfBirth>1959-11-20</dateOfBirth>
      </actor>
    </movie>

### YAML (534 bytes)

    title: Blade Runner
    year: 1982
    genres:
    - Sci-Fi
    - Thriller
    aspect-ratio: 2.39:1
    created: 2018-12-04T12:19:39Z
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
      age: 76
    - firstName: Rutger
      lastName: Hauer
      dateOfBirth: 1944-01-23
      age: 74
    - firstName: Sean
      lastName: Young
      dateOfBirth: 1959-11-20
      age: 59

## Build

### Gradle:
Build with gradle (default: _clean build_): 

    gradle

### Maven:
Build with Maven (default: _clean install_): 

    mvn
    
    