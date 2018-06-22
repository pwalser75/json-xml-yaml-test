package ch.frostnova.test.jackson.test.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * YAML utility functions
 *
 * @author pwalser
 * @since 22.06.2018
 */
public final class YamlUtil {

    private YamlUtil() {

    }

    /**
     * Convert the given object to YAML.
     *
     * @param value value
     * @param <T>   type
     * @return yaml
     */
    public static <T> String stringify(T value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper().writeValueAsString(value);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Convert the given JSON back to an object.
     *
     * @param type type
     * @param yaml yaml
     * @param <T>  type
     * @return object of the given type
     */
    public static <T> T parse(Class<T> type, String yaml) {
        if (yaml == null) {
            return null;
        }
        try {
            return objectMapper().readValue(yaml.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        return mapper;
    }
}
