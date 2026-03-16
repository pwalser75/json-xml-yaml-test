package ch.frostnova.test.jackson.test.util.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import static ch.frostnova.test.jackson.test.util.util.Unchecked.unchecked;
import static java.util.Objects.requireNonNull;

public class JacksonSerializer<T> implements Serializer<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public JacksonSerializer(ObjectMapper objectMapper, Class<T> type) {
        this.objectMapper = requireNonNull(objectMapper);
        this.type = requireNonNull(type);
    }

    @Override
    public byte[] serialize(T value) {
        return unchecked(() -> objectMapper.writeValueAsBytes(value));
    }

    @Override
    public T deserialize(byte[] serialized) {
        return unchecked(() -> objectMapper.readValue(serialized, type));
    }
}