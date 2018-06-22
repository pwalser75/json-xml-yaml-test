package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.domain.Movie;
import ch.frostnova.test.jackson.test.util.CollectionUtil;
import ch.frostnova.test.jackson.test.util.JsonUtil;
import ch.frostnova.test.jackson.test.util.XmlUtil;
import ch.frostnova.test.jackson.test.util.YamlUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test JSON / XML / YAML serialization
 *
 * @author pwalser
 * @since 22.06.2018
 */
public class SerializationTest {


    @Test
    public void testSerializeJSON() {

        Movie movie = Movie.create();

        String json = JsonUtil.stringify(movie);
        System.out.println(json);

        Movie parsed = JsonUtil.parse(Movie.class, json);
        verifyParsed(movie, parsed);
    }

    @Test
    public void testSerializeXML() {

        Movie movie = Movie.create();

        String xml = XmlUtil.stringify(movie);
        System.out.println(xml);

        Movie parsed = XmlUtil.parse(Movie.class, xml);
        verifyParsed(movie, parsed);
    }

    @Test
    public void testSerializeYAML() {

        Movie movie = Movie.create();

        String yaml = YamlUtil.stringify(movie);
        System.out.println(yaml);

        Movie parsed = YamlUtil.parse(Movie.class, yaml);
        verifyParsed(movie, parsed);
    }

    private void verifyParsed(Movie original, Movie parsed) {
        Assert.assertEquals(original.getTitle(), parsed.getTitle());
        Assert.assertEquals(original.getYear(), parsed.getYear());
        Assert.assertEquals(original.getSynopsis(), parsed.getSynopsis());
        Assert.assertEquals(original.getCreated(), parsed.getCreated());
        Assert.assertNotNull(parsed.getGenres());
        Assert.assertTrue(CollectionUtil.equals(original.getGenres(), parsed.getGenres()));
        Assert.assertTrue(CollectionUtil.equals(original.getRatings(), parsed.getRatings()));
    }
}
