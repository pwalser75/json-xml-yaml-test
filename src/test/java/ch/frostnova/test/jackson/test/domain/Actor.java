package ch.frostnova.test.jackson.test.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test value object, immutable
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "actor")
@JsonPropertyOrder({"firstName", "lastName", "birthDate", "rating"})
public class Actor {

    private final String firstName;

    private final String lastName;

    private final LocalDate birthDate;

    @JsonCreator
    public Actor(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("birthDate") LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(firstName, actor.firstName) &&
                Objects.equals(lastName, actor.lastName) &&
                Objects.equals(birthDate, actor.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate);
    }

    @Override
    public String toString() {
        return Stream.of(firstName, lastName).filter(Objects::nonNull).collect(Collectors.joining(" ")) + Optional.of(birthDate).map(d -> "[" + d + "]").orElse("");
    }
}