package ch.frostnova.test.jackson.test.util.domain;

import ch.frostnova.test.jackson.test.util.converter.FunctionalDeserializer;
import ch.frostnova.test.jackson.test.util.converter.FunctionalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author pwalser
 * @since 07.07.2018.
 */
@JsonSerialize(using = Genre.GenreSerializer.class)
@JsonDeserialize(using = Genre.GenreDeserializer.class)
public enum Genre {

    SCI_FI("Scii-Fi"),
    THRILLER("Thriller"),
    ACTION("Action"),
    COMEDY("Comedy"),
    ADVENTURE("Adventure"),
    FANTASY("Fantasy");

    public static class GenreSerializer extends FunctionalSerializer<Genre> {
        public GenreSerializer() {
            super(Genre::getIdentifier);
        }
    }

    public static class GenreDeserializer extends FunctionalDeserializer<Genre> {
        public GenreDeserializer() {
            super(Genre::byIdentifier);
        }
    }

    private final String identifier;

    Genre(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static Genre byIdentifier(String identifier) {
        return Stream.of(values()).filter(g -> g.identifier.equalsIgnoreCase(identifier))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No such value: " + identifier));
    }
}
