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

### JSON

    {
      "title" : "Blade Runner",
      "year" : 1982,
      "genres" : [ "Sci-Fi", "Thriller" ],
      "aspect-ratio" : "2.39:1",
      "created" : "2018-07-06T23:00:12Z",
      "ratings" : {
        "IMDB" : 8.2,
        "Metacritic" : 89.0
      },
      "synopsis" : "A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.",
      "actors" : [ {
        "firstName" : "Harrison",
        "lastName" : "Ford",
        "birthDate" : "1942-07-13"
      }, {
        "firstName" : "Rutger",
        "lastName" : "Hauer",
        "birthDate" : "1944-01-23"
      }, {
        "firstName" : "Sean",
        "lastName" : "Young",
        "birthDate" : "1959-11-20"
      } ]
    }

### XML

    <movie>
      <ratings IMDB="8.2" Metacritic="89.0"/>
      <created>2018-07-06T23:00:12Z</created>
      <title>Blade Runner</title>
      <year>1982</year>
      <genre>Sci-Fi</genre>
      <genre>Thriller</genre>
      <aspectRatio>2.39:1</aspectRatio>
      <synopsis>A blade runner must pursue and terminate four replicants
     who stole a ship in space and have returned to Earth to find their creator.</synopsis>
      <actor>
        <firstName>Harrison</firstName>
        <lastName>Ford</lastName>
        <birthDate>1942-07-13</birthDate>
      </actor>
      <actor>
        <firstName>Rutger</firstName>
        <lastName>Hauer</lastName>
        <birthDate>1944-01-23</birthDate>
      </actor>
      <actor>
        <firstName>Sean</firstName>
        <lastName>Young</lastName>
        <birthDate>1959-11-20</birthDate>
      </actor>
    </movie>

### YAML

    title: "Blade Runner"
    year: 1982
    genres:
    - "Sci-Fi"
    - "Thriller"
    aspect-ratio: "2.39:1"
    created: "2018-07-06T23:00:12Z"
    ratings:
      IMDB: 8.2
      Metacritic: 89.0
    synopsis: "A blade runner must pursue and terminate four replicants\n who stole a\
      \ ship in space and have returned to Earth to find their creator."
    actors:
    - firstName: "Harrison"
      lastName: "Ford"
      birthDate: "1942-07-13"
    - firstName: "Rutger"
      lastName: "Hauer"
      birthDate: "1944-01-23"
    - firstName: "Sean"
      lastName: "Young"
      birthDate: "1959-11-20"

## Build

### Gradle:
Build with gradle (default: _clean build_): 

    gradle

### Maven:
Build with Maven (default: _clean install_): 

    mvn
    
    