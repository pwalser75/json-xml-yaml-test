package ch.frostnova.test.jackson.test.util.domain;

import ch.frostnova.test.jackson.test.util.converter.DurationConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Test domain object, immutable - using internal builder.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "movie")
@JsonPropertyOrder({"title", "year", "genres", "aspectRatio", "rating"})
public class Movie {

    @JsonProperty("created")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true, localName = "created")
    private ZonedDateTime created = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).withZoneSameInstant(ZoneId.of("UTC"));

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title")
    private String title;

    @JsonProperty("year")
    @JacksonXmlProperty(localName = "year")
    private int year;

    @JsonProperty("genres")
    @JacksonXmlProperty(localName = "genre")
    private List<Genre> genres = new LinkedList<>();

    @JsonProperty("ratings")
    @JacksonXmlProperty(isAttribute = true)
    private Map<String, Number> ratings = new HashMap<>();

    @JsonProperty("synopsis")
    @JacksonXmlProperty(localName = "synopsis")
    private String synopsis;

    @JsonProperty("actors")
    @JacksonXmlProperty(localName = "actor")
    private List<Actor> actors = new LinkedList<>();

    @JsonProperty("aspect-ratio")
    @JacksonXmlProperty(localName = "aspectRatio")
    private AspectRatio aspectRatio;

    @JsonProperty("duration")
    @JacksonXmlProperty(localName = "duration")
    @JsonSerialize(using = DurationConverter.Serializer.class)
    @JsonDeserialize(using = DurationConverter.Deserializer.class)
    private Duration duration;

    @JsonProperty("metadata")
    @JacksonXmlProperty(localName = "metadata")
    private Metadata metadata = new Metadata();

    public static Movie create() {

        return Movie.builder()
                .title("Blade Runner")
                .year(1982)
                .addGenre(Genre.SCI_FI)
                .addGenre(Genre.THRILLER)
                .addRating("IMDB", 8.2)
                .addRating("Metacritic", 89d)
                .synopsis("A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.")
                .aspectRatio(new AspectRatio(2.39, 1))
                .duration(Duration.ofMinutes(117))
                .addActor(new Actor("Harrison", "Ford", LocalDate.of(1942, 7, 13)))
                .addActor(new Actor("Rutger", "Hauer", LocalDate.of(1944, 1, 23)))
                .addActor(new Actor("Sean", "Young", LocalDate.of(1959, 11, 20)))
                .addMetadata("director", "Ridley Scott")
                .addMetadata("screenplay", "Hampton Fancher, David Webb Peoples")
                .addMetadata("release-date", "1982-06-25")
                .get();
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Map<String, Number> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Number> ratings) {
        this.ratings = ratings;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Movie instance = new Movie();
        private boolean consumed;

        private Builder set(Consumer<Movie> access) {
            if (consumed) {
                throw new IllegalStateException("already consumed");
            }
            access.accept(instance);
            return this;
        }

        public Builder title(String title) {
            return set(x -> x.title = title);
        }

        public Builder year(int year) {
            return set(x -> x.year = year);
        }

        public Builder addGenre(Genre genre) {
            return set(x -> x.getGenres().add(genre));
        }

        public Builder addRating(String source, Number rating) {
            return set(x -> x.ratings.put(source, rating));
        }

        public Builder synopsis(String synopsis) {
            return set(x -> x.synopsis = synopsis);
        }

        public Builder addActor(Actor actor) {
            return set(x -> x.actors.add(actor));
        }

        public Builder aspectRatio(AspectRatio aspectRatio) {
            return set(x -> x.aspectRatio = aspectRatio);
        }

        public Builder duration(Duration duration) {
            return set(x -> x.duration = duration);
        }

        public Builder addMetadata(String key, String value) {
            return set(x -> x.metadata.set(key, value));
        }

        public Movie get() {
            consumed = true;
            return instance;
        }
    }
}