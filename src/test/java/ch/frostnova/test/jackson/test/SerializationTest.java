package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.CollectionUtil;
import ch.frostnova.test.jackson.test.util.SerialFormat;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.Assert;
import org.junit.Test;

import java.text.DecimalFormat;

/**
 * Test JSON / XML / YAML serialization
 *
 * @author pwalser
 * @since 22.06.2018
 */
public class SerializationTest {

    private final static DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");
    private final static long benchmarkTimeMs = 1000;

    @Test
    public void testJSON() {
        test(SerialFormat.json());
    }

    @Test
    public void testXML() {
        test(SerialFormat.xml());
    }

    @Test
    public void testYAML() {
        test(SerialFormat.yaml());
    }

    @Test
    public void testCBOR() {
        test(SerialFormat.cbor());
    }

    @Test
    public void testCSV() throws Exception {

        Movie movie = Movie.create();

        CsvSchema schema = CsvSchema.builder()
                .setUseHeader(true)
                .addColumn("title")
                .addColumn("year")
                .addColumn("aspect-ratio")
                .addArrayColumn("genres")
                .addColumn("created")
                .build();

        CsvMapper mapper = new CsvMapper();
        SerialFormat.configure(mapper);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

        String serialized = mapper.writerFor(Movie.class).with(schema).writeValueAsString(movie);
        System.out.println(serialized);

        MappingIterator<Movie> iter = mapper.readerFor(Movie.class).with(schema).readValues(serialized);
        Assert.assertTrue(iter.hasNext());
        Movie restored = iter.next();
        System.out.println(SerialFormat.json().stringify(restored));
        Assert.assertEquals(movie.getTitle(), restored.getTitle());
        Assert.assertEquals(movie.getYear(), restored.getYear());
        Assert.assertEquals(movie.getAspectRatio(), restored.getAspectRatio());
        Assert.assertTrue(CollectionUtil.equals(movie.getGenres(), restored.getGenres()));

        Assert.assertFalse(iter.hasNext());

    }

    private void test(SerialFormat format) {

        System.out.println("Testing format: " + format.getName());
        if (!format.isBinary()) {
            testSerializeText(format);
        }
        testSerialize(format);
        benchmark(format);
    }

    private void testSerialize(SerialFormat format) {

        Movie movie = Movie.create();
        byte[] serialized = format.serialize(movie);
        System.out.println(serialized.length + " bytes");
        Movie parsed = format.deserialize(Movie.class, serialized);
        verifyParsed(movie, parsed);
    }

    private void testSerializeText(SerialFormat format) {

        Movie movie = Movie.create();
        String serialized = format.stringify(movie);
        System.out.println(serialized);
        Movie parsed = format.parse(Movie.class, serialized);
        verifyParsed(movie, parsed);
    }

    private void benchmark(SerialFormat format) {

        Movie movie = Movie.create();
        long benchmarkTimeNs = benchmarkTimeMs * 1000000L;

        //warmup
        for (int i = 0; i < 100; i++) {
            byte[] serialized = format.serialize(movie);
            format.deserialize(Movie.class, serialized);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long time = System.nanoTime();
        long endTime = System.nanoTime() + benchmarkTimeNs;
        long samples = 0;
        while (System.nanoTime() < endTime) {
            format.serialize(movie);
            samples++;
        }
        time = System.nanoTime() - time;
        System.out.println(NUMBER_FORMAT.format(time / samples / 1000d) + " µS serialization (" + samples + " samples)");

        byte[] serialized = format.serialize(movie);
        time = System.nanoTime();

        endTime = System.nanoTime() + benchmarkTimeNs;
        samples = 0;
        while (System.nanoTime() < endTime) {
            format.deserialize(Movie.class, serialized);
            samples++;
        }

        time = System.nanoTime() - time;
        System.out.println(NUMBER_FORMAT.format(time / samples / 1000d) + " µS deserialization (" + samples + " samples)");
    }

    private void verifyParsed(Movie original, Movie parsed) {
        Assert.assertEquals(original.getTitle(), parsed.getTitle());
        Assert.assertEquals(original.getYear(), parsed.getYear());
        Assert.assertEquals(original.getSynopsis(), parsed.getSynopsis());
        Assert.assertEquals(original.getCreated(), parsed.getCreated());
        Assert.assertEquals(original.getAspectRatio(), parsed.getAspectRatio());
        Assert.assertNotNull(parsed.getGenres());
        Assert.assertTrue(CollectionUtil.equals(original.getGenres(), parsed.getGenres()));
        Assert.assertTrue(CollectionUtil.equals(original.getRatings(), parsed.getRatings()));
        Assert.assertTrue(CollectionUtil.equals(original.getActors(), parsed.getActors()));
        Assert.assertTrue(CollectionUtil.equals(original.getMetadata().getKeys(), parsed.getMetadata().getKeys()));
        for (String key : original.getMetadata().getKeys()) {
            Assert.assertEquals(original.getMetadata().get(key).get(), parsed.getMetadata().get(key).get());
        }

    }
}
