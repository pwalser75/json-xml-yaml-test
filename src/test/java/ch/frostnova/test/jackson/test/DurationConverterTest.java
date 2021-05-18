package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.converter.DurationConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link DurationConverter}
 *
 * @author pwalser
 * @since 06.07.2018
 */
public class DurationConverterTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Duration.class, new DurationConverter.Serializer());
        module.addDeserializer(Duration.class, new DurationConverter.Deserializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerialize() throws IOException {
        assertThat(objectMapper.writeValueAsString(Duration.ofNanos(0))).isEqualTo("\"0s\"");
        assertThat(objectMapper.writeValueAsString(Duration.ofSeconds(12345))).isEqualTo("\"3h 25m 45s\"");
        assertThat(objectMapper.writeValueAsString(Duration.ofDays(9).plus(Duration.ofHours(3)).plus(Duration.ofMinutes(4)).plus(Duration.ofSeconds(5)))).isEqualTo("\"1w 2d 3h 4m 5s\"");
        assertThat(objectMapper.writeValueAsString(Duration.ofDays(21).plus(Duration.ofMinutes(789)))).isEqualTo("\"3w 13h 9m\"");
        assertThat(objectMapper.writeValueAsString(Duration.ofDays(1).minus(Duration.ofSeconds(1)))).isEqualTo("\"23h 59m 59s\"");
    }

    @Test
    public void testDeserialize() throws IOException {
        assertThat(objectMapper.readValue("\"\"", Duration.class)).isNull();
        assertThat(objectMapper.readValue("\"0s\"", Duration.class)).isEqualTo(Duration.ofSeconds(0));
        assertThat(objectMapper.readValue("\"0m\"", Duration.class)).isEqualTo(Duration.ofSeconds(0));
        assertThat(objectMapper.readValue("\"0h\"", Duration.class)).isEqualTo(Duration.ofSeconds(0));
        assertThat(objectMapper.readValue("\"0d\"", Duration.class)).isEqualTo(Duration.ofSeconds(0));
        assertThat(objectMapper.readValue("\"0w\"", Duration.class)).isEqualTo(Duration.ofSeconds(0));

        assertThat(objectMapper.readValue("\"3h 25m 45s\"", Duration.class)).isEqualTo(Duration.ofSeconds(12345));
        assertThat(objectMapper.readValue("\"1w 2d 3h 4m 5s\"", Duration.class)).isEqualTo(Duration.ofDays(9).plus(Duration.ofHours(3)).plus(Duration.ofMinutes(4)).plus(Duration.ofSeconds(5)));
        assertThat(objectMapper.readValue("\"3w 13h 9m\"", Duration.class)).isEqualTo(Duration.ofDays(21).plus(Duration.ofMinutes(789)));
        assertThat(objectMapper.readValue("\"23h 59m 59s\"", Duration.class)).isEqualTo(Duration.ofDays(1).minus(Duration.ofSeconds(1)));

        assertThat(objectMapper.readValue("\"3h25m45s\"", Duration.class)).isEqualTo(Duration.ofSeconds(12345));
        assertThat(objectMapper.readValue("\"1w2d3h4m5s\"", Duration.class)).isEqualTo(Duration.ofDays(9).plus(Duration.ofHours(3)).plus(Duration.ofMinutes(4)).plus(Duration.ofSeconds(5)));
        assertThat(objectMapper.readValue("\"3w13h9m\"", Duration.class)).isEqualTo(Duration.ofDays(21).plus(Duration.ofMinutes(789)));
        assertThat(objectMapper.readValue("\"23h59m59s\"", Duration.class)).isEqualTo(Duration.ofDays(1).minus(Duration.ofSeconds(1)));
    }
}
