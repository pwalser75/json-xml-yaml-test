package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.ObjectMappers;
import ch.frostnova.test.jackson.test.util.domain.Movie;
import ch.frostnova.test.jackson.test.util.serializer.JacksonSerializer;
import ch.frostnova.test.jackson.test.util.serializer.MovieProtobufSerializer;
import ch.frostnova.test.jackson.test.util.serializer.Serializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.DecimalFormat;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JSON / YAML / XML / CBOR / Properties serialization
 *
 * @author pwalser
 * @since 22.06.2018
 */
class SerializationTest {

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");
    private static final long benchmarkTimeMs = 500;

    @Test
    void testJSON() {
        testFormat("JSON", new JacksonSerializer<>(ObjectMappers.json(), Movie.class), false);
    }

    @Test
    void testYAML() {
        testFormat("YAML", new JacksonSerializer<>(ObjectMappers.yaml(), Movie.class), false);
    }

    @Test
    void testXML() {
        testFormat("XML", new JacksonSerializer<>(ObjectMappers.xml(), Movie.class), false);
    }

    @Test
    void testCBOR() {
        testFormat("CBOR", new JacksonSerializer<>(ObjectMappers.cbor(), Movie.class), true);
    }

    @Test
    void testProperties() {
        testFormat("PROPERTIES", new JacksonSerializer<>(ObjectMappers.properties(), Movie.class), false);
    }

    @Test
    void testProtobuf() {
        testFormat("PROTOBUF", new MovieProtobufSerializer(), true);
    }

    @Test
    void testCSV() throws IOException {

        var movie = Movie.create();

        var schema = CsvSchema.builder()
                .setUseHeader(true)
                .addColumn("title")
                .addColumn("year")
                .addColumn("aspect-ratio")
                .addArrayColumn("genres")
                .addColumn("created")
                .build();

        var mapper = new CsvMapper();
        ObjectMappers.configure(mapper);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

        var serialized = mapper.writerFor(Movie.class).with(schema).writeValueAsString(movie);
        System.out.println(serialized);

        MappingIterator<Movie> iter = mapper.readerFor(Movie.class).with(schema).readValues(serialized);
        assertThat(iter.hasNext()).isTrue();
        var restored = iter.next();
        System.out.println(ObjectMappers.json().writeValueAsString(restored));
        assertThat(restored.getTitle()).isEqualTo(movie.getTitle());
        assertThat(restored.getYear()).isEqualTo(movie.getYear());
        assertThat(restored.getAspectRatio()).isEqualTo(movie.getAspectRatio());
        assertThat(restored.getGenres()).isEqualTo(movie.getGenres());

        assertThat(iter.hasNext()).isFalse();
    }

    void testFormat(String displayName, Serializer<Movie> serializer, boolean binary) {
        var value = Movie.create();
        testSerialize(serializer, binary);
        benchmark(serializer, value);
    }

    private void testSerialize(Serializer<Movie> serializer, boolean binary) {
        var movie = Movie.create();
        var serialized = serializer.serialize(movie);
        if (binary) {
            System.out.println(formatHex(serialized));
        } else {
            System.out.println(new String(serialized, UTF_8));
        }

        var parsed = serializer.deserialize(serialized);
        verifyParsed(movie, parsed);
    }

    private String formatHex(byte[] data) {
        var out = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i > 0 && (i & 31) == 0) {
                out.append("\n");
            }
            var hex = Integer.toHexString((data[i] + 256) & 0xFF).toUpperCase();
            if (hex.length() < 2) out.append('0');
            out.append(hex);
            out.append(" ");
        }
        return out.toString();
    }

    private <T> void benchmark(Serializer<T> serializer, T value) {

        var benchmarkTimeNs = benchmarkTimeMs * 1000000L;

        //warmup
        for (var i = 0; i < 1000; i++) {
            var serialized = serializer.serialize(value);
            serializer.deserialize(serialized);
        }

        var time = System.nanoTime();
        var endTime = System.nanoTime() + benchmarkTimeNs;
        long samples = 0;
        while (System.nanoTime() < endTime) {
            serializer.serialize(value);
            samples++;
        }
        time = System.nanoTime() - time;
        var serializationTimeNs = time / samples;

        var serialized = serializer.serialize(value);
        time = System.nanoTime();

        endTime = System.nanoTime() + benchmarkTimeNs;
        samples = 0;
        while (System.nanoTime() < endTime) {
            serializer.deserialize(serialized);
            samples++;
        }

        time = System.nanoTime() - time;
        var deserializationTimeNs = time / samples;
        var sizeBytes = serializer.serialize(value).length;

        System.out.printf("%d bytes, %.2f µS serialization, %.2f µS deserialization%n", sizeBytes, serializationTimeNs * 0.001, deserializationTimeNs * 0.001);
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
        for (var key : original.getMetadata().getKeys()) {
            assertThat(parsed.getMetadata().get(key)).isEqualTo(original.getMetadata().get(key));
        }
    }
}
