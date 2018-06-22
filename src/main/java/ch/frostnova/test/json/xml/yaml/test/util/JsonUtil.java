package ch.frostnova.test.json.xml.yaml.test.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * JSON utility functions
 *
 * @author pwalser
 * @since 23.01.2018
 */
public final class JsonUtil {

    private JsonUtil() {

    }

    /**
     * Convert the given object to JSON.
     *
     * @param value value
     * @param <T>   type
     * @return json json
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
     * @param json json
     * @param <T>  type
     * @return object of the given type
     */
    public static <T> T parse(Class<T> type, String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper().readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T parse(Class<T> type, InputStream in) {
        if (in == null) {
            return null;
        }
        return parse(type, read(in));
    }

    public static <T> T parse(Class<T> type, URL resource) throws IOException {
        if (resource == null) {
            return null;
        }
        try (InputStream in = resource.openStream()) {
            return parse(type, read(in));
        }
    }

    public static JsonNode parseTree(String json) {
        try {
            return objectMapper().readTree(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static JsonNode parseTree(InputStream in) {
        if (in == null) {
            return null;
        }
        return parseTree(read(in));
    }

    public static JsonNode parseTree(URL resource) throws IOException {
        if (resource == null) {
            return null;
        }
        try (InputStream in = resource.openStream()) {
            return parseTree(in);
        }
    }

    private static String read(InputStream in) {

        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
    }


    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }
}
