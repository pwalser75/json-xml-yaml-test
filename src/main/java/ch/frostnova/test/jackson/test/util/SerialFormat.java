package ch.frostnova.test.jackson.test.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Serial format with different flavours (JSON, XML, YAML), thread save.
 *
 * @author pwalser
 * @since 27.06.2018
 */
public abstract class SerialFormat {

    private static ThreadLocal<JSON> JSON_FORMAT = new ThreadLocal<>();
    private static ThreadLocal<XML> XML_FORMAT = new ThreadLocal<>();
    private static ThreadLocal<YAML> YAML_FORMAT = new ThreadLocal<>();

    private final String name;

    private SerialFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Convert the given object to JSON.
     *
     * @param value value
     * @param <T>   type
     * @return json json
     */
    public <T> String stringify(T value) {
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
     * Convert the given serialized data back to an object.
     *
     * @param type       type
     * @param serialized serialized data
     * @param <T>        type
     * @return object of the given type
     */
    public <T> T parse(Class<T> type, String serialized) {
        if (serialized == null) {
            return null;
        }
        try {
            return objectMapper().readValue(serialized.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T parse(Class<T> type, InputStream in) {
        if (in == null) {
            return null;
        }
        return parse(type, read(in));
    }

    public <T> T parse(Class<T> type, URL resource) throws IOException {
        if (resource == null) {
            return null;
        }
        try (InputStream in = resource.openStream()) {
            return parse(type, read(in));
        }
    }

    public JsonNode parseTree(String json) {
        try {
            return objectMapper().readTree(json);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JsonNode parseTree(InputStream in) {
        if (in == null) {
            return null;
        }
        return parseTree(read(in));
    }

    public JsonNode parseTree(URL resource) throws IOException {
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

    protected abstract ObjectMapper objectMapper();

    public static JSON json() {
        return lazyGet(JSON_FORMAT, JSON::new);
    }

    public static XML xml() {
        return lazyGet(XML_FORMAT, XML::new);
    }

    public static YAML yaml() {
        return lazyGet(YAML_FORMAT, YAML::new);
    }

    private static <F extends SerialFormat> F lazyGet(ThreadLocal<F> threadLocal, Supplier<F> factory) {
        return Optional.ofNullable(threadLocal.get()).orElseGet(() -> {
            F value = factory.get();
            threadLocal.set(value);
            return value;
        });
    }

    public static class JSON extends SerialFormat {

        private JSON() {
            super("JSON");
        }

        @Override
        protected ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        }
    }

    public static class XML extends SerialFormat {

        private XML() {
            super("XML");
        }

        @Override
        protected ObjectMapper objectMapper() {
            JacksonXmlModule xmlModule = new JacksonXmlModule();
            xmlModule.setDefaultUseWrapper(false);
            XmlMapper mapper = new XmlMapper(xmlModule);
            return configure(mapper);
        }
    }

    public static class YAML extends SerialFormat {

        private YAML() {
            super("YAML");
        }

        @Override
        protected ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        }
    }

    public static <T extends ObjectMapper> T configure(T mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }
}
