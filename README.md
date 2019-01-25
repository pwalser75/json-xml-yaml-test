#  JSON/XML/YAML Test
Testing Jackson JSON/XML/YAML object mapping

## Features

- shows how to properly configure the Jackson `ObjectMapper` (with Java 8 Date/Time API support)
- demonstrates how to serialize/deserialize JSON, XML and YAML with Jackson
- **bean** serialization &rarr; `Movie`, `Actor`
- custom serialization for **enums** using arbitrary string identifiers, ignoring case &rarr; `Genre`
- custom serialization for **domain object** &rarr; `Metadata` with `MetadataConverter.Serializer` and `MetadataConverter.Deserializer`
- custom serialization for **immutable value objects** with string constructor / `toString()` &rarr; `AspectRatio`
- function-based custom serialization using `FunctionalSerializer` / `FunctionalDeserializer`

## Examples

Serialized examples (`Movie` with `Actor`, `Genre`, `AspectRatio` and `Metadata`):

### JSON (894 bytes)

    {
      "title" : "Blade Runner",
      "year" : 1982,
      "genres" : [ "Sci-Fi", "Thriller" ],
      "aspect-ratio" : "2.39:1",
      "created" : "2019-01-25T11:22:24Z",
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
        "age" : 75
      }, {
        "firstName" : "Sean",
        "lastName" : "Young",
        "dateOfBirth" : "1959-11-20",
        "age" : 59
      } ],
      "metadata" : {
        "director" : "Ridley Scott",
        "release-date" : "1982-06-25",
        "screenplay" : "Hampton Fancher, David Webb Peoples"
      }
    }

### XML (1008 bytes)

    <movie>
      <ratings IMDB="8.2" Metacritic="89.0"/>
      <created>2019-01-25T11:22:24Z</created>
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
      <actor age="75">
        <firstName>Rutger</firstName>
        <lastName>Hauer</lastName>
        <dateOfBirth>1944-01-23</dateOfBirth>
      </actor>
      <actor age="59">
        <firstName>Sean</firstName>
        <lastName>Young</lastName>
        <dateOfBirth>1959-11-20</dateOfBirth>
      </actor>
      <metadata>
        <director>Ridley Scott</director>
        <release-date>1982-06-25</release-date>
        <screenplay>Hampton Fancher, David Webb Peoples</screenplay>
      </metadata>
    </movie>

### YAML (646 bytes)

    title: Blade Runner
    year: 1982
    genres:
    - Sci-Fi
    - Thriller
    aspect-ratio: 2.39:1
    created: 2019-01-25T11:22:24Z
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
      age: 75
    - firstName: Sean
      lastName: Young
      dateOfBirth: 1959-11-20
      age: 59
    metadata:
      director: Ridley Scott
      release-date: 1982-06-25
      screenplay: Hampton Fancher, David Webb Peoples

## Build

### Gradle:
Build with gradle (default: _clean build_): 

    gradle

### Maven:
Build with Maven (default: _clean install_): 

    mvn
    
    