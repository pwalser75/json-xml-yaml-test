package ch.frostnova.test.jackson.test;

import ch.frostnova.test.jackson.test.util.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

/**
 * Test for the JSON object mapper
 *
 * @author pwalser
 * @since 2018-01-05
 */
public class JsonObjectMapperTest {

    @Test
    public void testStringify() throws IOException {
        ObjectMapper objectMapper = ObjectMappers.json();

        assertThat(objectMapper.writeValueAsString(null)).isEqualTo("null");

        assertThat(objectMapper.writeValueAsString(123)).isEqualTo("123");
        assertThat(objectMapper.writeValueAsString("456")).isEqualTo("\"456\"");
        assertThat(objectMapper.writeValueAsString(true)).isEqualTo("true");
        assertThat(objectMapper.writeValueAsString("")).isEqualTo("\"\"");

        // arrays
        assertThat(objectMapper.writeValueAsString(asList(1, 2, 3, 4, 5))).isEqualTo("[ 1, 2, 3, 4, 5 ]");

        // maps
        Map<String, Object> map = new HashMap<>();
        map.put("a", true);
        map.put("b", 123);
        map.put("c", "456");
        map.put("d", true);
        map.put("e", asList(1, 2, 3));
        assertThat(objectMapper.writeValueAsString(map).replaceAll("\\s", "")).isEqualTo("{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}");
    }

    @Test
    public void testParse() throws IOException {
        ObjectMapper objectMapper = ObjectMappers.json();

        assertThat(objectMapper.readValue("null", Object.class)).isNull();
        assertThat(objectMapper.readValue("123", Integer.class)).isEqualTo(Integer.valueOf(123));
        assertThat(objectMapper.readValue("\"456\"", String.class)).isEqualTo("456");
        assertThat(objectMapper.readValue("true", Boolean.class)).isEqualTo(Boolean.TRUE);
        assertThat(objectMapper.readValue("\"\"", String.class)).isEqualTo("");

        // arrays
        assertThat(objectMapper.readValue("[1,2,3,4,5]", LinkedList.class)).containsExactly(1, 2, 3, 4, 5);

        // maps
        HashMap<?, ?> map = objectMapper.readValue("{\"a\":true,\"b\":123,\"c\":\"456\",\"d\":true,\"e\":[1,2,3]}", HashMap.class);
        assertThat(map.get("a")).isEqualTo(true);
        assertThat(map.get("b")).isEqualTo(123);
        assertThat(map.get("c")).isEqualTo("456");
        assertThat(map.get("d")).isEqualTo(Boolean.TRUE);
        assertThat(map.get("e")).isInstanceOf(List.class).asInstanceOf(LIST).containsExactly(1, 2, 3);
    }
}
