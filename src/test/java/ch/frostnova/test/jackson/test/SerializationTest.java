package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.ObjectMappers;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JSON / YAML / XML / CBOR / Properties serialization
 *
 * @author pwalser
 * @since 22.06.2018
 */
public class SerializationTest {

    private final static DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");
    private final static long benchmarkTimeMs = 500;

    @Test
    public void testJSON() throws IOException {
        testFormat("JSON", ObjectMappers.json(), false);
    }

    @Test
    public void testYAML() throws IOException {
        testFormat("YAML", ObjectMappers.yaml(), false);
    }

    @Test
    public void testXML() throws IOException {
        testFormat("XML", ObjectMappers.xml(), false);
    }

    @Test
    public void testCBOR() throws IOException {
        testFormat("CBOR", ObjectMappers.cbor(), true);
    }

    @Test
    public void testProperties() throws IOException {
        testFormat("PROPERTIES", ObjectMappers.properties(), false);
    }

    @Test
    public void testCSV() throws IOException {

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
        ObjectMappers.configure(mapper);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

        String serialized = mapper.writerFor(Movie.class).with(schema).writeValueAsString(movie);
        System.out.println(serialized);

        MappingIterator<Movie> iter = mapper.readerFor(Movie.class).with(schema).readValues(serialized);
        assertThat(iter.hasNext()).isTrue();
        Movie restored = iter.next();
        System.out.println(ObjectMappers.json().writeValueAsString(restored));
        assertThat(restored.getTitle()).isEqualTo(movie.getTitle());
        assertThat(restored.getYear()).isEqualTo(movie.getYear());
        assertThat(restored.getAspectRatio()).isEqualTo(movie.getAspectRatio());
        assertThat(restored.getGenres()).isEqualTo(movie.getGenres());

        assertThat(iter.hasNext()).isFalse();

    }

    void testFormat(String displayName, ObjectMapper objectMapper, boolean isBinary) throws IOException {

        System.out.println("Testing format: " + displayName);
        if (isBinary) {
            testSerializeBinary(objectMapper);
        } else {
            testSerializeText(objectMapper);
        }
        benchmark(objectMapper);
    }

    private void testSerializeBinary(ObjectMapper objectMapper) throws IOException {

        Movie movie = Movie.create();
        byte[] serialized = objectMapper.writeValueAsBytes(movie);
        System.out.println(serialized.length + " bytes");

        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        int index = 0;
        int read;
        while ((read = in.read()) >= 0) {
            String hex = Integer.toHexString(read).toUpperCase();
            while (hex.length() < 2) {
                hex = '0' + hex;
            }
            System.out.print(hex + " ");
            index++;
            if (index % 32 == 0) {
                System.out.println();
            }
        }
        System.out.println();

        Movie parsed = objectMapper.readValue(serialized, Movie.class);
        verifyParsed(movie, parsed);
    }

    private void testSerializeText(ObjectMapper objectMapper) throws IOException {

        Movie movie = Movie.create();
        System.out.println(objectMapper.writeValueAsBytes(movie).length + " bytes");
        String serialized = objectMapper.writeValueAsString(movie);
        System.out.println(serialized);
        Movie parsed = objectMapper.readValue(serialized, Movie.class);
        verifyParsed(movie, parsed);
    }

    private void benchmark(ObjectMapper objectMapper) throws IOException {

        Movie movie = Movie.create();
        long benchmarkTimeNs = benchmarkTimeMs * 1000000L;

        //warmup
        for (int i = 0; i < 100; i++) {
            byte[] serialized = objectMapper.writeValueAsBytes(movie);
            objectMapper.readValue(serialized, Movie.class);
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
            objectMapper.writeValueAsBytes(movie);
            samples++;
        }
        time = System.nanoTime() - time;
        System.out.println(NUMBER_FORMAT.format(time / samples / 1000d) + " µS serialization (" + samples + " samples)");

        byte[] serialized = objectMapper.writeValueAsBytes(movie);
        time = System.nanoTime();

        endTime = System.nanoTime() + benchmarkTimeNs;
        samples = 0;
        while (System.nanoTime() < endTime) {
            objectMapper.readValue(serialized, Movie.class);
            samples++;
        }

        time = System.nanoTime() - time;
        System.out.println(NUMBER_FORMAT.format(time / samples / 1000d) + " µS deserialization (" + samples + " samples)");
    }

    private void verifyParsed(Movie original, Movie parsed) {
        assertThat(parsed.getTitle()).isEqualTo(original.getTitle());
        assertThat(parsed.getYear()).isEqualTo(original.getYear());
        assertThat(parsed.getSynopsis()).isEqualTo(original.getSynopsis());
        assertThat(parsed.getCreated()).isEqualTo(original.getCreated());
        assertThat(parsed.getAspectRatio()).isEqualTo(original.getAspectRatio());
        assertThat(parsed.getGenres()).isNotNull();
        assertThat(parsed.getGenres()).containsExactlyElementsOf(original.getGenres());
        assertThat(parsed.getRatings()).containsExactlyInAnyOrderEntriesOf(original.getRatings());
        assertThat(parsed.getActors()).containsExactlyElementsOf(original.getActors());
        assertThat(parsed.getMetadata().getKeys()).containsExactlyInAnyOrderElementsOf(original.getMetadata().getKeys());
        for (String key : original.getMetadata().getKeys()) {
            assertThat(parsed.getMetadata().get(key)).isEqualTo(original.getMetadata().get(key));
        }
    }
}
