package ch.frostnova.test.jackson.test.util.serializer;

public interface Serializer<T> {

    byte[] serialize(T value);

    T deserialize(byte[] serialized);
}
