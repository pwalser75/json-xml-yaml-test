package ch.frostnova.test.json.xml.yaml.test;

import ch.frostnova.test.json.xml.yaml.test.domain.Movie;
import ch.frostnova.test.json.xml.yaml.test.util.CollectionUtil;
import ch.frostnova.test.json.xml.yaml.test.util.JsonUtil;
import ch.frostnova.test.json.xml.yaml.test.util.XmlUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test JSON / XML / YAML serialization
 *
 * @author wap
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

    private void verifyParsed(Movie original, Movie parsed) {
        Assert.assertEquals(original.getTitle(), parsed.getTitle());
        Assert.assertEquals(original.getYear(), parsed.getYear());
        Assert.assertEquals(original.getSynopsis(), parsed.getSynopsis());
        Assert.assertNotNull(parsed.getGenres());
        Assert.assertTrue(CollectionUtil.equals(original.getGenres(), parsed.getGenres()));
        Assert.assertTrue(CollectionUtil.equals(original.getRatings(), parsed.getRatings()));
    }
}
