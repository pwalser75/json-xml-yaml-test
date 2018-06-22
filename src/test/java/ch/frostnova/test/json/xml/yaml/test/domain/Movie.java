package ch.frostnova.test.json.xml.yaml.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Test bean with JAXB mappings.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "movie")
@JsonPropertyOrder({"title", "year", "genres", "rating"})
public class Movie {

    @JsonProperty("title")
    @JacksonXmlProperty(isAttribute = true, localName = "title")
    private String title;

    @JsonProperty("year")
    @JacksonXmlProperty(isAttribute = true, localName = "year")
    private int year;

    @JsonProperty("genres")
    @JacksonXmlProperty(localName = "genre")
    private List<String> genres = new LinkedList<>();

    @JsonProperty("ratings")
    @JacksonXmlProperty(isAttribute = true)
    private Map<String, Number> ratings = new HashMap<>();

    @JsonProperty("synopsis")
    @JacksonXmlProperty(localName = "synopsis")
    private String synopsis;

    public static Movie create() {

        Movie movie = new Movie();
        movie.setTitle("Blade Runner");
        movie.setYear(1982);
        movie.setGenres(Arrays.asList("Sci-Fi", "Thriller"));
        movie.getRatings().put("IMDB", 8.2);
        movie.getRatings().put("Metacritic", 89d);
        movie.setSynopsis("A blade runner must pursue and terminate four replicants\n who stole a ship in space and have returned to Earth to find their creator.");
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
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
}