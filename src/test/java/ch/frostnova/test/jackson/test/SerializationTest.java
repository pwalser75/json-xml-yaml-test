package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.domain.Movie;
import ch.frostnova.test.jackson.test.util.CollectionUtil;
import ch.frostnova.test.jackson.test.util.SerialFormat;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Test JSON / XML / YAML serialization
 *
 * @author pwalser
 * @since 22.06.2018
 */
public class SerializationTest {


    @Test
    public void testSerializeJSON() {
        testSerialize(SerialFormat.json());
    }

    @Test
    public void testSerializeXML() {
        testSerialize(SerialFormat.xml());
    }

    @Test
    public void testSerializeYAML() {
        testSerialize(SerialFormat.yaml());
    }

    private void testSerialize(SerialFormat format) {

        System.out.println("Testing format: " + format.getName());
        Movie movie = Movie.create();

        String serialized = format.stringify(movie);
        System.out.println(serialized);
        System.out.println(serialized.getBytes(StandardCharsets.UTF_8).length + " bytes");

        Movie parsed = format.parse(Movie.class, serialized);
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
        Assert.assertTrue(CollectionUtil.equals(original.getActors(), parsed.getActors()));
    }
}
