package ch.frostnova.test.jackson.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ch.frostnova.test.jackson.test.util.ObjectMappers.Type.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.MINIMIZE_QUOTES;

/**
 * Lazy-created object mappers for various serialization formats.
 *
 * @author pwalser
 * @since 2021-05-09
 */
public final class ObjectMappers {

    enum Type {
        JSON, //  JavaScript Object Notation
        YAML, //  Yet Another Markup Language
        XML, //  eXtensible Markup Language
        PROPERTIES, // Java Properties format
        CBOR, // Concise Binary Object Representation (https://www.rfc-editor.org/info/rfc7049)
        PROTOBUF // Google Protobuffer format
    }

    private static final Map<Type, ObjectMapper> objectMappers = new ConcurrentHashMap<>();

    private ObjectMappers() {

    }

    public static ObjectMapper json() {
        return objectMappers.computeIfAbsent(JSON, type -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        });
    }

    public static ObjectMapper yaml() {
        return objectMappers.computeIfAbsent(YAML, type -> {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(MINIMIZE_QUOTES));
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        });
    }

    public static ObjectMapper xml() {
        return objectMappers.computeIfAbsent(XML, type -> {
            JacksonXmlModule xmlModule = new JacksonXmlModule();
            xmlModule.setDefaultUseWrapper(false);
            XmlMapper mapper = new XmlMapper(xmlModule);
            return configure(mapper);
        });
    }

    public static ObjectMapper cbor() {
        return objectMappers.computeIfAbsent(CBOR, type -> {
            ObjectMapper mapper = new ObjectMapper(new CBORFactory());
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        });
    }


    public static ObjectMapper protobuf() {
        return objectMappers.computeIfAbsent(PROTOBUF, type -> {
            ObjectMapper mapper = new ProtobufMapper();
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        });
    }

    public static ObjectMapper properties() {
        return objectMappers.computeIfAbsent(PROPERTIES, type -> {
            ObjectMapper mapper = new JavaPropsMapper();
            mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
            return configure(mapper);
        });
    }

    public static ObjectMapper configure(ObjectMapper mapper) {
        return mapper
                .registerModule(new JavaTimeModule())
                .setDateFormat(new StdDateFormat())
                .enable(INDENT_OUTPUT)
                .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(WRITE_DATES_AS_TIMESTAMPS)
                .disable(WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(NON_EMPTY);
    }
}
