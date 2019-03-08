package ch.frostnova.test.jackson.test.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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

    private static Map<Class<? extends SerialFormat>, SerialFormat> serialFormats = new HashMap<>();

    private final String name;
    private final boolean binary;

    private SerialFormat(String name, boolean binary) {
        this.name = name;
        this.binary = binary;
    }

    public String getName() {
        return name;
    }

    public boolean isBinary() {
        return binary;
    }

    /**
     * Convert the given object to a serialized string.
     *
     * @param value value
     * @param <T>   type
     * @return string serialized string
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
     * Serialize the given object
     *
     * @param value value
     * @param <T>   type
     * @return string serialized format
     */
    public <T> byte[] serialize(T value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper().writeValueAsBytes(value);
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
    public <T> T deserialize(Class<T> type, byte[] serialized) {
        if (serialized == null) {
            return null;
        }
        try {
            return objectMapper().readValue(serialized, type);
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
        return deserialize(type, serialized.getBytes(StandardCharsets.UTF_8));
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
        return lazyGet(JSON.class, JSON::new);
    }

    public static XML xml() {
        return lazyGet(XML.class, XML::new);
    }

    public static YAML yaml() {
        return lazyGet(YAML.class, YAML::new);
    }

    public static CBOR cbor() {
        return lazyGet(CBOR.class, CBOR::new);
    }

    private static <F extends SerialFormat> F lazyGet(Class<F> format, Supplier<F> factory) {

        return Optional.ofNullable(serialFormats.get(format)).map(format::cast).orElseGet(() -> {
            F instance = factory.get();
            serialFormats.put(format, instance);
            return instance;
        });
    }

    public static class JSON extends SerialFormat {

        private JSON() {
            super("JSON", false);
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
            super("XML", false);
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
            super("YAML", false);
        }

        @Override
        protected ObjectMapper objectMapper() {
            YAMLFactory yamlFactory = new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
            ObjectMapper mapper = new ObjectMapper(yamlFactory);
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        }
    }

    /**
     * CBOR = Concise Binary Object Representation (https://www.rfc-editor.org/info/rfc7049)
     */
    public static class CBOR extends SerialFormat {

        private CBOR() {
            super("CBOR", true);
        }

        @Override
        protected ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper(new CBORFactory());
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
