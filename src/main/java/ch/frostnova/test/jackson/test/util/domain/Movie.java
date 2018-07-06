package ch.frostnova.test.jackson.test.util.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Test domain object
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "movie")
@JsonPropertyOrder({"title", "year", "genres", "rating"})
public class Movie {

    @JsonProperty("created")
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

    @JsonProperty("aspectRatio")
    @JacksonXmlProperty(localName = "aspectRatio")
    private AspectRatio aspectRatio;

    public static Movie create() {

        Movie movie = new Movie();
        movie.setTitle("Blade Runner");
        movie.setYear(1982);
        movie.setGenres(Arrays.asList(Genre.SCI_FI, Genre.THRILLER));
        movie.getRatings().put("IMDB", 8.2);
        movie.getRatings().put("Metacritic", 89d);
        movie.setSynopsis("A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.");
        movie.setAspectRatio(new AspectRatio(2.39, 1));
        movie.getActors().add(new Actor("Harrison", "Ford", LocalDate.of(1942, 7, 13)));
        movie.getActors().add(new Actor("Rutger", "Hauer", LocalDate.of(1944, 1, 23)));
        movie.getActors().add(new Actor("Sean", "Young", LocalDate.of(1959, 11, 20)));
        return movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        title = name;
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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}